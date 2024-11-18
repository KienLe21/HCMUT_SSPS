package com.hcmut.ssps_server.service.implement;

import com.hcmut.ssps_server.dto.response.AdminPrintingLogReportResponse;
import com.hcmut.ssps_server.dto.response.AdminPrintingLogResponse;
import com.hcmut.ssps_server.dto.response.ReportItem;
import com.hcmut.ssps_server.enums.Frequency;
import com.hcmut.ssps_server.exception.AppException;
import com.hcmut.ssps_server.exception.ErrorCode;
import com.hcmut.ssps_server.model.Printing;
import com.hcmut.ssps_server.model.PrintingLog;
import com.hcmut.ssps_server.repository.PrintingLogRepository;
import com.hcmut.ssps_server.repository.UserRepository.StudentRepository;
import com.hcmut.ssps_server.service.interf.IPrintingLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PrintingLogService implements IPrintingLogService {
    private PrintingLogRepository printingLogRepository;

    private StudentRepository studentRepository;

    @Override
    public void addPrintingLog(Printing printing) {
        PrintingLog printingLog = new PrintingLog();
        printingLog.setDocument(printing.getDocument());
        printingLog.setAdminPrintMail(printing.getAdminPrintMail());
        printingLog.setPrintingStartTime(printing.getPrintingStartTime());
        printingLog.setPrintingEndTime(LocalDateTime.now());
        printingLog.setStudent(studentRepository.findByUser_Email(printing.getStudentUploadMail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        printingLog.setPrinterToPrintID(printing.getPrinterToPrintID());
        printingLogRepository.save(printingLog);
    }

    /**
     * Func: viewAllPrintLog
     *
     * @param startDate LocalDate
     * @param endDate   LocalDate
     * @return List<AdminPrintingLogResponse>
     */
    @Override
    public Page<AdminPrintingLogResponse> viewAllPrintLog(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return printingLogRepository.viewAllPrintLog(startDate, endDate, pageable);
    }

    /**
     * Func: viewPrintLog
     *
     * @param printingLogId Long
     * @return AdminPrintingLogResponse
     */
    @Override
    public AdminPrintingLogResponse viewPrintLog(Long printingLogId) {
        printingLogRepository.findById(printingLogId)
                .orElseThrow(() -> new AppException(ErrorCode.PRINTING_LOG_ID_NOT_FOUND));
        return printingLogRepository.viewPrintLog(printingLogId);
    }

    @Override
    public AdminPrintingLogReportResponse generateUsageReports(Frequency frequency) {
        List<Object[]> reportData;

        switch (frequency) {
            case MONTHLY:
                reportData = printingLogRepository.countUsersAndPagesByMonth();
                break;
            case QUARTERLY:
                reportData = printingLogRepository.countUsersAndPagesByQuarter();
                break;
            case YEARLY:
                reportData = printingLogRepository.countUsersAndPagesByYear();
                break;
            default:
                throw new IllegalArgumentException("Unsupported frequency: " + frequency);
        }

        return buildReportResponse(reportData, frequency);
    }

    private AdminPrintingLogReportResponse buildReportResponse(List<Object[]> reportData, Frequency frequency) {
        List<ReportItem> items = new ArrayList<>();

        for (Object[] row : reportData) {
            ReportItem item = new ReportItem();

            item.setYear(((Number) row[0]).intValue());

            if (frequency == Frequency.MONTHLY) {
                item.setMonth(((Number) row[1]).intValue());
                item.setUserCount((Long) row[2]);
                item.setTotalPageCount(((BigDecimal) row[3]).longValue());
            } else if (frequency == Frequency.QUARTERLY) {
                item.setQuarter(((Number) row[1]).intValue());
                item.setUserCount((Long) row[2]);
                item.setTotalPageCount(((BigDecimal) row[3]).longValue());
            } else if (frequency == Frequency.YEARLY) {
                item.setUserCount((Long) row[1]);
                item.setTotalPageCount(((BigDecimal) row[2]).longValue());
            }

            items.add(item);
        }

        AdminPrintingLogReportResponse response = new AdminPrintingLogReportResponse();
        response.setFrequency(frequency);
        response.setItems(items);
        return response;
    }

}