package com.hcmut.ssps_server.service.implement;

import com.hcmut.ssps_server.exception.AppException;
import com.hcmut.ssps_server.exception.ErrorCode;
import com.hcmut.ssps_server.model.Document;
import com.hcmut.ssps_server.model.Printing;
import com.hcmut.ssps_server.repository.PrinterRepository;
import com.hcmut.ssps_server.repository.PrintingRepository;
import com.hcmut.ssps_server.service.interf.IPrintingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PrintingService implements IPrintingService {
    PrintingRepository printingRepository;
    PrinterRepository printerRepository;

    @Override
    public void addPrintRequest(Document document, int printerId) {
        Printing printing = new Printing();
        printing.setDocument(document);
        printing.setPrintingStartTime(LocalDateTime.now());
        printing.setPrinterToPrintID(printerId);
        var context = SecurityContextHolder.getContext();
        printing.setStudentUploadMail(context.getAuthentication().getName());
        printingRepository.save(printing);
    }

    @Override
    public Page<Printing> getPrintRequestsByPrinterId(int printerId, Pageable pageable) {
        if (!printerRepository.existsByPrinterID(printerId)) {
            throw new AppException(ErrorCode.PRINTER_NOT_FOUND);
        }
        Page<Printing> printingPage = printingRepository.findAllByPrinterToPrintIDAndExpiredTimeIsNull(printerId, pageable);

        return printingPage;
    }

    //3 TRƯỜNG HỢP: STUDENT CONFIRM RECEIVE DOC hoặc MÁY IN BỊ LỖI NÊN HỦY YÊU CẦU ĐANG TỒN TẠI hoặc TÀI LIỆU HẾT THỜI GIAN TỒN TẠI
//    public void deletePrintRequest(Long printingId) {
//
//    }

    @Override
    public Page<Printing> getAllPrintRequests(Pageable pageable) {
        Page<Printing> printingPage = printingRepository.findAllByExpiredTimeIsNull(pageable);
        if (printingPage.isEmpty()) {
            throw new AppException(ErrorCode.PRINT_REQUEST_NOT_FOUND);
        }

        return printingPage;
    }
}