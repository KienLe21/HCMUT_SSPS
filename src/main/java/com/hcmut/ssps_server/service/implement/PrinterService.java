package com.hcmut.ssps_server.service.implement;


import com.hcmut.ssps_server.dto.request.UploadConfigRequest;
import com.hcmut.ssps_server.dto.request.PrinterCreationRequest;
import com.hcmut.ssps_server.enums.PrinterStatus;
import com.hcmut.ssps_server.exception.AppException;
import com.hcmut.ssps_server.exception.ErrorCode;
import com.hcmut.ssps_server.model.Printer;
import com.hcmut.ssps_server.enums.PrintableStatus;
import com.hcmut.ssps_server.model.Printing;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.repository.PrinterRepository;
import com.hcmut.ssps_server.repository.PrintingRepository;
import com.hcmut.ssps_server.repository.UserRepository.StudentRepository;
import com.hcmut.ssps_server.repository.UserRepository.UserRepository;
import com.hcmut.ssps_server.service.EmailService;
import com.hcmut.ssps_server.service.interf.IPrinterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ReflectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PrinterService implements IPrinterService {
    PrintingRepository printingRepository;
    PrintingLogService printingLogService;
    PrinterRepository printerRepo;
    StudentRepository studentRepo;
    UserRepository userRepository;
    EmailService emailService;

    @Override
    public Printer addPrinter(PrinterCreationRequest request) {
        Printer printer = new Printer();
        printer.setPrinterLocation(request.getPrinterLocation());
        printer.setStatus(request.getStatus());
        printer.setPapersLeft(request.getPapersLeft());
        printer.setAvailableDocType(request.getAvailableDocType());
        return printerRepo.save(printer);
    }
    @Override
    public Printer updatePrinter(Long printerId, Map<String, Object> updates) {
        Printer printer = printerRepo.findById(printerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRINTER_NOT_FOUND));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Printer.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, printer, value);
            }
        });

        return printerRepo.save(printer);
    }

    @Override
    public Printer getPrinter(Long printerId) {
        return printerRepo.findById(printerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRINTER_NOT_FOUND));
    }

    @Override
    public List<Printer> getAllPrinters(Pageable pageable) {
        return printerRepo.findAll(pageable).getContent();
    }

    @Override
    public void deletePrinter(Long printerId) {
        if (!printerRepo.existsById(printerId)) {
            throw new AppException(ErrorCode.PRINTER_NOT_FOUND);
        }
        List<Printing> printRequests = printingRepository.findByPrinterToPrintID(printerId.intValue());
        for (Printing printing : printRequests) {
            int refundPages = printing.getDocument().getPageCount();
            //ADD PAGES BACK TO STUDENT
            Student student = studentRepo.findByUser_Email(printing.getStudentUploadMail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            student.setNumOfPages(student.getNumOfPages() + refundPages);

            printingRepository.deleteById((long)printing.getId());
        }
        printerRepo.deleteById(printerId);
    }

    @Override
    public void enablePrinter(Long printerId) {
        Printer printer = printerRepo.findById(printerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRINTER_NOT_FOUND));
        printer.setStatus(PrinterStatus.ONLINE);
        printerRepo.save(printer);
    }

    @Override
    public void disablePrinter(Long printerId) {
        Printer printer = printerRepo.findById(printerId)
                .orElseThrow(() -> new AppException(ErrorCode.PRINTER_NOT_FOUND));
        printer.setStatus(PrinterStatus.OFFLINE);
        printerRepo.save(printer);
    }

    @Override
    public String print(int printerId) {
        List<Printing> printRequests= printingRepository.findByPrinterToPrintID(printerId);

        if (printRequests.isEmpty())
            return "Not found any print requests to print";

        for (Printing printing : printRequests) {
            if (printing.getExpiredTime() == null) {
                var context = SecurityContextHolder.getContext();
                printing.setAdminPrintMail(context.getAuthentication().getName());
                printing.setExpiredTime(LocalDateTime.now().plusHours(2));
                printingRepository.save(printing);
                printingLogService.addPrintingLog(printing);

                //SEND EMAIL TO STUDENT
                var user = userRepository.findByEmail(printing.getStudentUploadMail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                var document = printing.getDocument();
                String subject = emailService.generateEmailSubject(document.getFileName());
                String content = emailService.generateEmailContent(user.getFullName(), document.getFileName());
                emailService.sendEmail(user.getEmail(), subject, content);
            }
        }
        return "Printer " + printerId + " printed successfully";
    }

    @Override
    public PrintableStatus isPrintable(MultipartFile file, UploadConfigRequest uploadConfigRequest) throws IOException {
        Printer printer = printerRepo.findById((long) uploadConfigRequest.getPrinterId()).orElseThrow(() -> new AppException(ErrorCode.PRINTER_NOT_FOUND));
        if (printer == null) {
            return PrintableStatus.PRINTER_NOT_FOUND;
        }
        List<String> availableDocType = printer.getAvailableDocType();
        //CHECK FILE AVAILABLE
        String fileType = file.getContentType();
        if(availableDocType.contains(fileType)){
            //CHECK PAPER
            int printerPapers = printer.getPapersLeft();
            int requiredPagesForPrinter = caculateRequiredPages(file, uploadConfigRequest);
            int requiredPagesForStudent = (uploadConfigRequest.isColorPrint())? requiredPagesForPrinter * 2 : requiredPagesForPrinter;

            //CHECK STUDENT'S PAPERS
            var context = SecurityContextHolder.getContext();
            Student student = studentRepo.findByUser_Email(context.getAuthentication().getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            if (student.getNumOfPages() < requiredPagesForStudent) {
                return PrintableStatus.STUDENT_NOT_HAVE_ENOUGH_PAGES;
            }

            //CHECK PRINTER'S PAPERS
            if (printerPapers >= requiredPagesForPrinter) {
                //MINUS PRINTER PAPERS
                printer.setPapersLeft(printerPapers - requiredPagesForPrinter);
                printerRepo.save(printer);

                //MINUS STUDENT PAPERS
                student.setNumOfPages(student.getNumOfPages() - requiredPagesForStudent);
                studentRepo.save(student);
                return PrintableStatus.PRINTABLE;
            } else {
                //SHOULD ADD AN NOTIFY FUNCTION
                return PrintableStatus.PRINTER_NOT_HAVE_ENOUGH_PAPERS;
            }
        }
        else
            return PrintableStatus.UNSUPPORTED_FILE_TYPE;
    }

    public int caculatePage(String fileType, InputStream inputStream) throws IOException {
        switch (fileType) {
            case "application/pdf":
                return calculatePageCountForPdf(inputStream);

            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return calculatePageCountForWord(inputStream);

            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return calculatePageCountForExcel(inputStream);

            case "image/tiff":
                return calculatePageCountForTiff(inputStream);

            case "image/jpeg":
            case "image/png":
            case "image/gif":
                return 1;
            default:
                throw new UnsupportedOperationException("Unsupported file type: " + fileType);
        }
    }

    public int calculatePageCountForTiff(InputStream inputStream) throws IOException {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream)) {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("TIFF");

            if (!readers.hasNext()) {
                throw new IOException("No ImageReader found for TIFF format");
            }

            ImageReader reader = readers.next();
            reader.setInput(imageInputStream, true);
            int pageCount = reader.getNumImages(true);
            reader.dispose();
            return pageCount;
        }
    }

    public int calculatePageCountForExcel(InputStream inputStream) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            return workbook.getNumberOfSheets(); // Trả về số trang tính (sheets) trong file Excel
        }
    }

    public int calculatePageCountForWord(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            return document.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
        }
    }

    public int calculatePageCountForPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            return document.getNumberOfPages();
        }
    }

    public int caculateRequiredPages(MultipartFile file, UploadConfigRequest uploadConfigRequest) throws IOException {
        int docPages = caculatePage(file.getContentType(), file.getInputStream());
        int changeUpToSidedType = switch (uploadConfigRequest.getSidedType()) {
            case "double" -> 2;
            default -> 1;
        };
        int changeUptoPaperSize = switch (uploadConfigRequest.getPaperSize()) {
            case "A3" -> 2;
            default -> 1;
        };
        int numberOfCopies = uploadConfigRequest.getNumberOfCopies();
        return docPages * changeUptoPaperSize * numberOfCopies / changeUpToSidedType;
    }

    @Override
    public List<Printer> findMatchPrinters(List<String> requiredDocumentType) {
        return printerRepo.findAll().stream()
                .filter(printer -> printer.getAvailableDocType().containsAll(requiredDocumentType))
                .collect(Collectors.toList());
    }

}
