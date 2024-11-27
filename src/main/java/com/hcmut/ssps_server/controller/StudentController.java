package com.hcmut.ssps_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmut.ssps_server.dto.request.PrinterRequest;
import com.hcmut.ssps_server.dto.request.StudentCreationRequest;
import com.hcmut.ssps_server.dto.request.RatingCreationRequest;
import com.hcmut.ssps_server.dto.request.UploadConfigRequest;
import com.hcmut.ssps_server.dto.response.*;
import com.hcmut.ssps_server.model.Document;
import com.hcmut.ssps_server.model.Printer;
import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.service.implement.PrinterService;
import com.hcmut.ssps_server.service.interf.IPrinterService;
import com.hcmut.ssps_server.service.implement.RatingService;
import com.hcmut.ssps_server.service.interf.IRatingService;
import com.hcmut.ssps_server.service.interf.IStudentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class StudentController {
    IStudentService studentService;
    ObjectMapper objectMapper = new ObjectMapper();
    private final PrinterService printerService;
    private final RatingService ratingService;

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
    public ApiResponse<List<Printer>> matchPrinters(@RequestBody PrinterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Printer> printerPage = printerService.findMatchPrinters(request.getRequiredDocumentType(), pageable);
        return ApiResponse.<List<Printer>>builder()
                .result(printerPage.getContent())
                .currentPage(printerPage.getNumber())
                .totalElements(printerPage.getTotalElements())
                .totalPages(printerPage.getTotalPages())
                .build();
    }

    @GetMapping("/get-printer/{printerId}")
    ApiResponse<Printer> getPrinter(@PathVariable Long printerId) {
        return ApiResponse.<Printer>builder()
                .result(printerService.getPrinter(printerId))
                .build();
    }

    @GetMapping("/get-all-printers")
    ApiResponse<List<Printer>> getAllPrinters(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Printer> printerPage = printerService.getAllPrinters(pageable);
        return ApiResponse.<List<Printer>>builder()
                .result(printerPage.getContent())
                .currentPage(printerPage.getNumber())
                .totalElements(printerPage.getTotalElements())
                .totalPages(printerPage.getTotalPages())
                .build();
    }

    @GetMapping("/get-print-requests")
    public ApiResponse<PrintRequestResponse> getPrintRequests() {
        // Fetch the printing for the student
        PrintRequestResponse printRequestResponse = studentService.getPrintRequests();
        return ApiResponse.<PrintRequestResponse>builder()
                .result(printRequestResponse)
                .build();
    }

    @PostMapping("/create-rating")
    public ApiResponse<RatingResponse> createRating(@RequestBody @Valid RatingCreationRequest request) {
        Rating createdRating = ratingService.createRating(request);
        RatingResponse response = new RatingResponse();
        response.setId(createdRating.getId());
        response.setRating(createdRating.getRating());
        response.setComment(createdRating.getComment());
        response.setPrintingId((long) createdRating.getPrinting().getId());

        return ApiResponse.<RatingResponse>builder()
                .result(response)
                .build();
    }


    @GetMapping("/ratings/my-ratings")
    public ApiResponse<List<RatingResponse>> getRatingOfCurrentStudent() {
        List<RatingResponse> ratings = ratingService.getRatingOfCurrentStudent();
        return ApiResponse.<List<RatingResponse>>builder()
                .result(ratings)
                .build();
    }


    @PatchMapping("/update-rating/{ratingId}")
    public ApiResponse<RatingResponse> updateRating(@PathVariable Long ratingId, @RequestBody Map<String, Object> updates) {
        RatingResponse ratingResponse = ratingService.updateRating(ratingId, updates);
        return ApiResponse.<RatingResponse>builder()
                .result(ratingResponse)
                .build();
    }

    @DeleteMapping("/delete-rating/{ratingId}")
    ApiResponse<String> deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ApiResponse.<String>builder()
                .result("Rating deleted successfully")
                .build();
    }

    @GetMapping("/get-rating-by-rating-id/{ratingId}")
    ApiResponse<RatingResponse> getRatingByRatingId(@PathVariable Long ratingId) {
        return ApiResponse.<RatingResponse>builder()
                .result(ratingService.getRatingByRatingId(ratingId))
                .build();
    }

    @GetMapping("/get-rating-by-printing-id/{printingId}")
    ApiResponse<List<AdminRatingResponse>> getRatingByPrintingId(@PathVariable Long printingId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminRatingResponse> ratingPages = ratingService.getRatingByPrintingId(printingId, pageable);
        return ApiResponse.<List<AdminRatingResponse>>builder()
                .result(ratingPages.getContent())
                .currentPage(ratingPages.getNumber())
                .totalElements(ratingPages.getTotalElements())
                .totalPages(ratingPages.getTotalPages())
                .build();
    }
}
