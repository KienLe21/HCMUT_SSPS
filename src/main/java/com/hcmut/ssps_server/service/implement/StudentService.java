package com.hcmut.ssps_server.service.implement;

import com.hcmut.ssps_server.dto.request.StudentCreationRequest;
import com.hcmut.ssps_server.dto.request.UploadConfigRequest;
import com.hcmut.ssps_server.dto.response.PrintingLogResponse;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.exception.AppException;
import com.hcmut.ssps_server.exception.ErrorCode;
import com.hcmut.ssps_server.mapper.StudentMapper;
import com.hcmut.ssps_server.mapper.UserMapper;
import com.hcmut.ssps_server.model.Document;
import com.hcmut.ssps_server.enums.PrintableStatus;
import com.hcmut.ssps_server.model.Printer;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.model.user.User;
import com.hcmut.ssps_server.repository.DocumentRepository;
import com.hcmut.ssps_server.repository.PrinterRepository;
import com.hcmut.ssps_server.repository.PrintingRepository;
import com.hcmut.ssps_server.repository.UserRepository.StudentRepository;
import com.hcmut.ssps_server.repository.UserRepository.UserRepository;
import com.hcmut.ssps_server.service.interf.IStudentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentService implements IStudentService {
    UserRepository userRepository;
    UserMapper userMapper;
    StudentMapper studentMapper;
    StudentRepository studentRepository;
    DocumentRepository documentRepo;
    PrinterService printerService;
    PrintingService printingService;
    PrinterRepository printerRepository;
    PrintingRepository printingRepository;

    @Override
    public Student createStudent(StudentCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setRole("STUDENT");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Student student = new Student();
        student.setUser(user);
        student.setNumOfPages(0);
        student.setStudentId(Long.parseLong(request.getStudentId()));

        userRepository.save(user);
        return studentRepository.save(student);
    }

    @Override
    public StudentResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        log.info("authority: " + context.getAuthentication().getAuthorities());
        Student student = studentRepository.findByUser_Email(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return studentMapper.toStudentResponse(student);
    }

    //CHƯA CÓ CÁC HÀM NHƯ CONFIRM RECEIVE DOC

    @Override
    public String uploadDocument(MultipartFile file, UploadConfigRequest uploadConfig) throws IOException {
        int printerId = uploadConfig.getPrinterId();
        String fileType = file.getContentType();
        PrintableStatus printable = printerService.isPrintable(file, uploadConfig);

        if (printable == PrintableStatus.PRINTABLE) {
            Document document = new Document();
            document.setFileName(file.getOriginalFilename());
            document.setFileType(fileType);
            document.setPageCount(printerService.caculateRequiredPages(file, uploadConfig));
            document.setPaperSize(uploadConfig.getPaperSize());
            document.setSidedType(uploadConfig.getSidedType());
            document.setNumberOfCopies(uploadConfig.getNumberOfCopies());
            document.setColorPrint(uploadConfig.isColorPrint());

            //Save document to database
            documentRepo.save(document);

            //Store ALL print request to Printing
            printingService.addPrintRequest(document, printerId);

            return "Upload success";

        } else if (printable == PrintableStatus.UNSUPPORTED_FILE_TYPE) {
            return "Printer is not supported";

        } else if (printable == PrintableStatus.PRINTER_NOT_HAVE_ENOUGH_PAPERS) {
            return "Printer doesn't have enough papers to print this document";

        } else if (printable == PrintableStatus.STUDENT_NOT_HAVE_ENOUGH_PAGES) {
            return "Student's account doesn't have enough pages to print this document";
        } else {
            //Nearly not exist this case
            return "Printer not found";
        }
    }


    @Override
    // Method to check remaining pages for the logged-in student
    public Integer checkRemainingPages() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Student student = studentRepository.findByUser_Email(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return student.getNumOfPages();
    }

    @Override
    public Integer recharge(int amount) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Student student = studentRepository.findByUser_Email(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // 3. Quy đổi số tiền thành số trang và cập nhật lại numOfPages
        int additionalPages = amount / 1000; // 1 trang = 1000 đồng
        student.setNumOfPages(student.getNumOfPages() + additionalPages);

        // 4. Lưu lại thay đổi trong database
        studentRepository.save(student);

        // 5. Trả về số trang mới của sinh viên sau khi nạp tiền
        return student.getNumOfPages();
    }

    @Override
    public List<PrintingLogResponse> getPrintingLogsForStudent() {
        // Retrieve the student based on the email from the context holder
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Student student = studentRepository.findByUser_Email(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Convert each PrintingLog to PrintingLogResponse
        return student.getLogList().stream()
                .map(log -> new PrintingLogResponse(
                        log.getId(),
                        log.getDocument().getFileName(),
                        log.getDocument().getPageCount(),
                        log.getPrintingStartTime(),
                        log.getPrintingEndTime(),
                        log.getPrinterToPrintID()))
                .collect(Collectors.toList());
    }

    @Override
    public String confirm(Long printingId) {
        try{
            printingRepository.findById(printingId).orElseThrow(() -> new AppException(ErrorCode.PRINT_REQUEST_NOT_FOUND));
            printingRepository.deleteById(printingId);
            return "Receive document successfully";
        } catch (Exception e){
            return e.getMessage();
        }
    }
}
