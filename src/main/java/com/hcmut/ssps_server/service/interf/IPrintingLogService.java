package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.dto.response.AdminPrintingLogReportResponse;
import com.hcmut.ssps_server.dto.response.AdminPrintingLogResponse;
import com.hcmut.ssps_server.enums.Frequency;
import com.hcmut.ssps_server.model.Printing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IPrintingLogService {
    void addPrintingLog(Printing printing);

    Page<AdminPrintingLogResponse> viewAllPrintLog(LocalDate startDate, LocalDate endDate, Pageable pageable);

    AdminPrintingLogResponse viewPrintLog(Long printingLogId);

    AdminPrintingLogReportResponse generateUsageReports(Frequency frequency);
}