package com.hcmut.ssps_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmut.ssps_server.dto.request.StudentCreationRequest;
import com.hcmut.ssps_server.dto.request.UploadConfigRequest;
import com.hcmut.ssps_server.dto.response.ApiResponse;
import com.hcmut.ssps_server.dto.response.PageResponse;
import com.hcmut.ssps_server.dto.response.PrintingLogResponse;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.model.Document;
import com.hcmut.ssps_server.model.Printer;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.service.implement.PrinterService;
import com.hcmut.ssps_server.service.interf.IPrinterService;
import com.hcmut.ssps_server.service.interf.IStudentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class StudentController {
    IStudentService studentService;
    ObjectMapper objectMapper = new ObjectMapper();
    private final PrinterService printerService;

    @PostMapping("/register")
    public ApiResponse<Student> createStudent(@RequestBody @Valid StudentCreationRequest request) {
        return ApiResponse.<Student>builder()
                .result(studentService.createStudent(request))
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<StudentResponse> getMyInfo() {
        return ApiResponse.<StudentResponse>builder()
                .result(studentService.getMyInfo())
                .build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadDocument(@RequestParam("file") MultipartFile file,
                                              @RequestParam("uploadConfig") String uploadConfigJson) throws IOException {
        UploadConfigRequest uploadConfig = objectMapper.readValue(uploadConfigJson, UploadConfigRequest.class);
        return ApiResponse.<String>builder()
                .result(studentService.uploadDocument(file, uploadConfig))
                .build();
    }

    @GetMapping("/remain-pages")
    public ApiResponse<PageResponse> getRemainPages() {
        int remainingPages = studentService.checkRemainingPages();
        PageResponse pageResponse = PageResponse.builder()
                .RemainingPages(remainingPages)
                .build();

        return ApiResponse.<PageResponse>builder()
                .result(pageResponse)
                .build();
    }
    @PostMapping("/recharge")
    public ApiResponse<PageResponse> recharge(@RequestParam int amount) {
        int remainingPages = studentService.recharge(amount);
        PageResponse pageResponse = PageResponse.builder()
                .RemainingPages(remainingPages)
                .build();

        return ApiResponse.<PageResponse>builder()
                .result(pageResponse)
                .build();
    }
    @GetMapping("/print-logs")
    public ApiResponse<List<PrintingLogResponse>> viewPrintLog() {
        // Get the logged-in user’s email from SecurityContextHolder


        // Fetch the printing logs for the student
        List<PrintingLogResponse> logs = studentService.getPrintingLogsForStudent();

        return ApiResponse.<List<PrintingLogResponse>>builder()
                .result(logs)
                .build();
    }

    @PostMapping("/confirm-receive")
    public ApiResponse<String> confirmReceive(@RequestParam int printingId) {
        return ApiResponse.<String>builder()
                .result(studentService.confirm((long) printingId))
                .build();
    }

    @PostMapping("/match-printers")
    public ApiResponse<List<Printer>> matchPrinters(@RequestBody @Valid List<String> requiredDocumentType) {
        return ApiResponse.<List<Printer>>builder()
                .result(printerService.findMatchPrinters(requiredDocumentType))
                .build();
    }
}
