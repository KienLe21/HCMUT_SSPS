package com.hcmut.ssps_server.controller;

import com.hcmut.ssps_server.dto.request.UserCreationRequest;
import com.hcmut.ssps_server.dto.request.UserUpdateRequest;
import com.hcmut.ssps_server.dto.request.PrinterCreationRequest;
import com.hcmut.ssps_server.dto.response.ApiResponse;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.dto.response.UserResponse;
import com.hcmut.ssps_server.model.Printer;
import com.hcmut.ssps_server.model.user.User;
import com.hcmut.ssps_server.service.interf.IAdminService;
import com.hcmut.ssps_server.service.interf.IPrinterService;
import com.hcmut.ssps_server.service.interf.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AdminController {
    IAdminService adminService;
    IUserService userService;
    IPrinterService printerService;

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
    ApiResponse<List<StudentResponse>> getAllStudents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<List<StudentResponse>>builder()
                .result(adminService.getAllStudents(pageable))
                .build();
    }

    @PutMapping("/update-user/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateRequest request ) {
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
        printerService.print(printerId);
        return ApiResponse.<String>builder()
                .result("Printer " + printerId + " printed successfully")
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
    ApiResponse<List<Printer>> getAllPrinters(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<List<Printer>>builder()
                .result(printerService.getAllPrinters(pageable))
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
    @DeleteMapping("/document-expired")
    ApiResponse<String> checkExpiredDocument() {
        return ApiResponse.<String>builder()
                .result(adminService.checkExpiredDocument())
                .build();
    }

    @GetMapping("/match-printers")
    public ApiResponse<List<Printer>> matchPrinters(@RequestParam List<String> requiredDocumentType) {
        return ApiResponse.<List<Printer>>builder()
                .result(printerService.findMatchPrinters(requiredDocumentType))
                .build();
    }
}
