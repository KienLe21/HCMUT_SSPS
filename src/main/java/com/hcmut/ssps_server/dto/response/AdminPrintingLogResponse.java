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
    private Integer numberOfCopies;
    private Integer pageCount;
    private String paperSize;
    private String sidedType;
    private LocalDate expiredTime;
    private Long studentId;
    private Integer numOfPages;
    private String email;
}
