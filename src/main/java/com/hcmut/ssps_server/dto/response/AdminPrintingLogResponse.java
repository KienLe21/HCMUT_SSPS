package com.hcmut.ssps_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPrintingLogResponse {
    private Long printingLogId;
    private String adminPrintMail;
    private Integer printerId;
    private LocalDate printingEndTime;
    private LocalDate printingStartTime;
    private Long documentId;
    private String fileName;
    private String fileType;
    private Integer numberOfCopies;
    private Integer pageCount;
    private String paperSize;
    private String sidedType;
    private Integer printingId;
    private String printingAdminMail;
    private LocalDate expiredTime;
    private Integer printerToPrintId;
    private LocalDate printingStart;
    private String studentUploadMail;
    private Long studentId;
    private Integer numOfPages;
    private Long userId;
    private String email;
    private String fullName;
    private String role;
}
