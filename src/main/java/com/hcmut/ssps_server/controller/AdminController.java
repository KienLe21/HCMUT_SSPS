package com.hcmut.ssps_server.controller;

import com.hcmut.ssps_server.dto.request.PrinterCreationRequest;
import com.hcmut.ssps_server.dto.request.PrinterRequest;
import com.hcmut.ssps_server.dto.request.UserCreationRequest;
import com.hcmut.ssps_server.dto.request.UserUpdateRequest;
import com.hcmut.ssps_server.dto.response.*;
import com.hcmut.ssps_server.enums.Frequency;
import com.hcmut.ssps_server.model.Printer;
import com.hcmut.ssps_server.model.Printing;
import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.model.user.User;
import com.hcmut.ssps_server.service.implement.RatingService;
import com.hcmut.ssps_server.service.interf.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import  java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AdminController {
    IAdminService adminService;

    IUserService userService;

    IPrinterService printerService;
    private final RatingService ratingService;

    IPrintingLogService printingLogService;

    IPrintingService printingService;

    @PostMapping("/register")
    ApiResponse<User> createAdmin(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<User>builder()
                .result(adminService.createAdmin(request))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(adminService.getMyInfo())
                .build();
    }

    @GetMapping("/get-student/{studentId}")
    ApiResponse<StudentResponse> getUser(@PathVariable("studentId") Long studentId) {
        return ApiResponse.<StudentResponse>builder()
                .result(adminService.getStudent(studentId))
                .build();
    }

    @GetMapping("/get-all-students")
    ApiResponse<List<StudentResponse>> getAllStudents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentResponse> studentPage = adminService.getAllStudents(pageable);
        return ApiResponse.<List<StudentResponse>>builder()
                .result(studentPage.getContent())
                .currentPage(studentPage.getNumber())
                .totalElements(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .build();
    }

    @PutMapping("/update-user/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/delete-user/{userId}")
    ApiResponse<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User deleted")
                .build();
    }

    @PostMapping("/print/{printerId}")
    ApiResponse<String> print(@PathVariable("printerId") int printerId) {
        return ApiResponse.<String>builder()
                .result(printerService.print(printerId))
                .build();
    }

    @PostMapping("/add-printer")
    ApiResponse<Printer> addPrinter(@RequestBody @Valid PrinterCreationRequest request) {
        return ApiResponse.<Printer>builder()
                .result(printerService.addPrinter(request))
                .build();
    }

    @PatchMapping("/update-printer/{printerId}")
    ApiResponse<Printer> updatePrinter(@PathVariable Long printerId, @RequestBody Map<String, Object> updates) {
        return ApiResponse.<Printer>builder()
                .result(printerService.updatePrinter(printerId, updates))
                .build();
    }

    @GetMapping("/get-printer/{printerId}")
    ApiResponse<Printer> getPrinter(@PathVariable Long printerId) {
        return ApiResponse.<Printer>builder()
                .result(printerService.getPrinter(printerId))
                .build();
    }

    @GetMapping("/get-all-printers")
    ApiResponse<List<Printer>> getAllPrinters(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Printer> printerPage = printerService.getAllPrinters(pageable);
        return ApiResponse.<List<Printer>>builder()
                .result(printerPage.getContent())
                .currentPage(printerPage.getNumber())
                .totalElements(printerPage.getTotalElements())
                .totalPages(printerPage.getTotalPages())
                .build();
    }

    @DeleteMapping("/delete-printer/{printerId}")
    ApiResponse<String> deletePrinter(@PathVariable Long printerId) {
        printerService.deletePrinter(printerId);
        return ApiResponse.<String>builder()
                .result("Printer deleted successfully")
                .build();
    }

    @PutMapping("/enable-printer/{printerId}")
    ApiResponse<String> enablePrinter(@PathVariable Long printerId) {
        printerService.enablePrinter(printerId);
        return ApiResponse.<String>builder()
                .result("Printer enabled successfully")
                .build();
    }

    @PutMapping("/disable-printer/{printerId}")
    ApiResponse<String> disablePrinter(@PathVariable Long printerId) {
        printerService.disablePrinter(printerId);
        return ApiResponse.<String>builder()
                .result("Printer disabled successfully")
                .build();
    }

    /**
     * API cho phép SPSO xem toàn bộ nhật ký của các hoạt động in ấn, có thể bao gồm thông tin chi tiết như ngày giờ,
     * số trang in và thông tin người dùng.
     *
     * @param startDate Ngày bắt đầu tìm kiếm
     * @param endDate   Ngày kết thúc tìm kiếm
     * @return Danh sách printing_log join với document join printing join student join user
     */
    @GetMapping("/view-print-logs")
    ApiResponse<Page<AdminPrintingLogResponse>> viewAllPrintLog(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<Page<AdminPrintingLogResponse>>builder()
                .result(printingLogService.viewAllPrintLog(startDate, endDate, pageable))
                .build();
    }

    /**
     * API cho phép SPSO xem chi tiết nhật ký của các hoạt động in ấn, có thể bao gồm thông tin chi tiết như ngày giờ,
     * số trang in và thông tin người dùng.
     *
     * @param printingLogId Id của bản ghi printing Log Id
     * @return Thông tin printing_log join với document join printing join student join user
     */
    @GetMapping("/view-print-log/{printingLogId}")
    ApiResponse viewPrintLog(
            @PathVariable Optional<Long> printingLogId
    ) {
        if (printingLogId.isEmpty()) {
            return ApiResponse.builder()
                    .result("PrintingLogId is required in the URL path")
                    .build();
        }
        return ApiResponse.<AdminPrintingLogResponse>builder()
                .result(printingLogService.viewPrintLog(printingLogId.get()))
                .build();
    }

    /**
     * API Phương thức này chịu trách nhiệm tạo hoặc cập nhật các báo cáo sử dụng dựa trên dữ liệu mới nhất.
     * Nó giúp SPSO theo dõi các hoạt động in ấn và hiểu được nhu cầu sử dụng dịch vụ.
     *
     * @param frequency Hằng số bao gồm Tháng, Quý, Năm
     * @return Thông tin số User sử dụng và Số trang in theo Hằng số
     */
    @GetMapping("/generate-usage-reports")
    public ApiResponse generateUsageReports(
            @RequestParam(required = false) Frequency frequency
    ) {
        if (frequency == null) {
            return ApiResponse.builder()
                    .result("Frequency parameter is required")
                    .build();
        }
        return ApiResponse.<AdminPrintingLogReportResponse>builder()
                .result(printingLogService.generateUsageReports(frequency))
                .build();
    }

    @PostMapping("/match-printers")
    public ApiResponse<List<Printer>> matchPrinters(@RequestBody PrinterRequest request) {
        Integer page = request.getPage();
        if (page == null) page = 0;
        Integer size = request.getSize();
        if (size == null) size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Printer> printerPage = printerService.findMatchPrinters(request.getRequiredDocumentType(), pageable);
        return ApiResponse.<List<Printer>>builder()
                .result(printerPage.getContent())
                .currentPage(printerPage.getNumber())
                .totalElements(printerPage.getTotalElements())
                .totalPages(printerPage.getTotalPages())
                .build();
    }

    @GetMapping("/get-print-requests/{printerId}")
    public ApiResponse<List<Printing>> getPrintRequestsByPrinterId(@PathVariable int printerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Printing> printingPage = printingService.getPrintRequestsByPrinterId(printerId, pageable);
        return ApiResponse.<List<Printing>>builder()
                .result(printingPage.getContent())
                .currentPage(printingPage.getNumber())
                .totalElements(printingPage.getTotalElements())
                .totalPages(printingPage.getTotalPages())
                .build();
    }

    @GetMapping("/get-all-print-requests")
    public ApiResponse<List<Printing>> getAllPrintRequests(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Printing> printingPage = printingService.getAllPrintRequests(pageable);
        return ApiResponse.<List<Printing>>builder()
                .result(printingPage.getContent())
                .currentPage(printingPage.getNumber())
                .totalElements(printingPage.getTotalElements())
                .totalPages(printingPage.getTotalPages())
                .build();
    }

    @DeleteMapping("/document-expired")
    ApiResponse<String> checkExpiredDocument() {
        return ApiResponse.<String>builder()
                .result(adminService.checkExpiredDocument())
                .build();
    }

    @GetMapping("/get-all-ratings")
    ApiResponse<List<AdminRatingResponse>> getAllRatings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminRatingResponse> ratingPages = ratingService.getAllRatings(pageable);
        return ApiResponse.<List<AdminRatingResponse>>builder()
                .result(ratingPages.getContent())
                .currentPage(ratingPages.getNumber())
                .totalElements(ratingPages.getTotalElements())
                .totalPages(ratingPages.getTotalPages())
                .build();
    }

    @GetMapping("/get-rating-by-printingLog-id/{printingLogId}")
    ApiResponse<List<AdminRatingResponse>> getRatingByPrintingLogId(@PathVariable Long printingLogId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminRatingResponse> ratingPages = ratingService.getRatingByPrintingLogId(printingLogId, pageable);
        return ApiResponse.<List<AdminRatingResponse>>builder()
                .result(ratingPages.getContent())
                .currentPage(ratingPages.getNumber())
                .totalElements(ratingPages.getTotalElements())
                .totalPages(ratingPages.getTotalPages())
                .build();
    }

    @GetMapping("/get-ratings-by-student-id/{studentId}")
    ApiResponse<List<AdminRatingResponse>> getRatingsByStudentId(@PathVariable Long studentId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminRatingResponse> ratingPages = ratingService.getRatingsByStudentId(studentId, pageable);
        return ApiResponse.<List<AdminRatingResponse>>builder()
                .result(ratingPages.getContent())
                .currentPage(ratingPages.getNumber())
                .totalElements(ratingPages.getTotalElements())
                .totalPages(ratingPages.getTotalPages())
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
}
