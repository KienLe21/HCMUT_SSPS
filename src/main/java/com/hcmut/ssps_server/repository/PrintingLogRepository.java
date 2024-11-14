package com.hcmut.ssps_server.repository;

import com.hcmut.ssps_server.dto.response.AdminPrintingLogResponse;
import com.hcmut.ssps_server.model.PrintingLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface PrintingLogRepository extends JpaRepository<PrintingLog, Long> {

    @Query(value = "SELECT " +
            "    pl.id AS printing_log_id, " +
            "    pl.admin_print_mail, " +
            "    pl.printer_id, " +
            "    pl.printing_end_time, " +
            "    pl.printing_start_time, " +
            "    d.id AS document_id, " +
            "    d.file_name, " +
            "    d.file_type, " +
            "    d.number_of_copies, " +
            "    d.page_count, " +
            "    d.paper_size, " +
            "    d.sided_type, " +
            "    p.id AS printing_id, " +
            "    p.admin_print_mail AS printing_admin_mail, " +
            "    p.expired_time, " +
            "    p.printer_to_printid, " +
            "    p.printing_start_time AS printing_start, " +
            "    p.student_upload_mail, " +
            "    s.student_id, " +
            "    s.num_of_pages, " +
            "    u.id AS user_id, " +
            "    u.email, " +
            "    u.full_name, " +
            "    u.role " +
            "FROM " +
            "    printing_log pl " +
            "LEFT JOIN " +
            "    document d ON pl.document_id = d.id " +
            "LEFT JOIN " +
            "    printing p ON p.document_id = d.id " +
            "LEFT JOIN " +
            "    student s ON pl.student_id = s.student_id " +
            "LEFT JOIN " +
            "    user u ON s.user_id = u.id " +
            "WHERE " +
            "    (:startDate IS NULL OR p.printing_start_time >= :startDate) AND " +
            "    (:endDate IS NULL OR p.printing_start_time <= :endDate)",
            countQuery = "SELECT COUNT(pl.id) " +
                    "FROM printing_log pl " +
                    "LEFT JOIN document d ON pl.document_id = d.id " +
                    "LEFT JOIN printing p ON p.document_id = d.id " +
                    "LEFT JOIN student s ON pl.student_id = s.student_id " +
                    "LEFT JOIN user u ON s.user_id = u.id " +
                    "WHERE (:startDate IS NULL OR p.printing_start_time >= :startDate) " +
                    "AND (:endDate IS NULL OR p.printing_start_time <= :endDate)",
            nativeQuery = true)
    Page<Object[]> viewAllPrintLogRaw(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    default Page<AdminPrintingLogResponse> viewAllPrintLog(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Object[]> rawData = viewAllPrintLogRaw(startDate, endDate, pageable);
        List<AdminPrintingLogResponse> adminPrintingLogResponses = new ArrayList<>();

        for (Object[] row : rawData.getContent()) {
            Long printingLogId = ((Number) row[0]).longValue();
            String adminPrintMail = (String) row[1];
            Integer printerId = ((Number) row[2]).intValue();
            LocalDate printingEndTime = row[3] != null ? ((Timestamp) row[3]).toLocalDateTime().toLocalDate() : null;
            LocalDate printingStartTime = row[4] != null ? ((Timestamp) row[4]).toLocalDateTime().toLocalDate() : null;
            Long documentId = ((Number) row[5]).longValue();
            String fileName = (String) row[6];
            String fileType = (String) row[7];
            Integer numberOfCopies = ((Number) row[8]).intValue();
            Integer pageCount = ((Number) row[9]).intValue();
            String paperSize = (String) row[10];
            String sidedType = (String) row[11];
            Integer printingId = ((Number) row[12]).intValue();
            String printingAdminMail = (String) row[13];
            LocalDate expiredTime = row[14] != null ? ((Timestamp) row[14]).toLocalDateTime().toLocalDate() : null;
            Integer printerToPrintId = ((Number) row[15]).intValue();
            LocalDate printingStart = ((Timestamp) row[16]).toLocalDateTime().toLocalDate();
            String studentUploadMail = (String) row[17];
            Long studentId = row[18] != null ? ((Number) row[18]).longValue() : null;
            Integer numOfPages = row[19] != null ? ((Number) row[19]).intValue() : null;
            Long userId = row[20] != null ? ((Number) row[20]).longValue() : null;
            String email = (String) row[21];
            String fullName = (String) row[22];
            String role = (String) row[23];

            AdminPrintingLogResponse response = new AdminPrintingLogResponse(
                    printingLogId, adminPrintMail, printerId, printingEndTime, printingStartTime, documentId, fileName,
                    fileType, numberOfCopies, pageCount, paperSize, sidedType, printingId, printingAdminMail, expiredTime,
                    printerToPrintId, printingStart, studentUploadMail, studentId, numOfPages, userId, email, fullName, role
            );
            adminPrintingLogResponses.add(response);
        }

        return new PageImpl<>(adminPrintingLogResponses, pageable, rawData.getTotalElements());
    }

    @Query(value = "SELECT \n" +
            "    pl.id AS printing_log_id,\n" +
            "    pl.admin_print_mail,\n" +
            "    pl.printer_id,\n" +
            "    pl.printing_end_time,\n" +
            "    pl.printing_start_time,\n" +
            "    d.id AS document_id,\n" +
            "    d.file_name,\n" +
            "    d.file_type,\n" +
            "    d.number_of_copies,\n" +
            "    d.page_count,\n" +
            "    d.paper_size,\n" +
            "    d.sided_type,\n" +
            "    p.id AS printing_id,\n" +
            "    p.admin_print_mail AS printing_admin_mail,\n" +
            "    p.expired_time,\n" +
            "    p.printer_to_printid,\n" +
            "    p.printing_start_time AS printing_start,\n" +
            "    p.student_upload_mail,\n" +
            "    s.student_id,\n" +
            "    s.num_of_pages,\n" +
            "    u.id AS user_id,\n" +
            "    u.email,\n" +
            "    u.full_name,\n" +
            "    u.role\n" +
            "FROM \n" +
            "    printing_log pl\n" +
            "LEFT JOIN \n" +
            "    document d ON pl.document_id = d.id\n" +
            "LEFT JOIN \n" +
            "    printing p ON p.document_id = d.id\n" +
            "LEFT JOIN \n" +
            "    student s ON pl.student_id = s.student_id\n" +
            "LEFT JOIN \n" +
            "    user u ON s.user_id = u.id WHERE pl.id = :printingLogId \n",
            nativeQuery = true)
    List<Object[]> viewPrintLogRaw(
            @Param("printingLogId") Long printingLogId
    );

    default AdminPrintingLogResponse viewPrintLog(
            @Param("printingLogId") Long printingLogId
    ) {
        List<Object[]> rawData = viewPrintLogRaw(printingLogId);
        Object[] row = rawData.getFirst();
//            Long printingLogId = ((Number) row[0]).longValue();
        String adminPrintMail = (String) row[1];
        Integer printerId = ((Number) row[2]).intValue();
        LocalDate printingEndTime = row[3] != null ? ((Timestamp) row[3]).toLocalDateTime().toLocalDate() : null;
        LocalDate printingStartTime = row[4] != null ? ((Timestamp) row[4]).toLocalDateTime().toLocalDate() : null;
        Long documentId = ((Number) row[5]).longValue();
        String fileName = (String) row[6];
        String fileType = (String) row[7];
        Integer numberOfCopies = ((Number) row[8]).intValue();
        Integer pageCount = ((Number) row[9]).intValue();
        String paperSize = (String) row[10];
        String sidedType = (String) row[11];
        Integer printingId = ((Number) row[12]).intValue();
        String printingAdminMail = (String) row[13];
        LocalDate expiredTime = row[14] != null ? ((Timestamp) row[14]).toLocalDateTime().toLocalDate() : null;
        Integer printerToPrintId = ((Number) row[15]).intValue();
        LocalDate printingStart = ((Timestamp) row[16]).toLocalDateTime().toLocalDate();
        String studentUploadMail = (String) row[17];
        Long studentId = row[18] != null ? ((Number) row[18]).longValue() : null;
        Integer numOfPages = row[19] != null ? ((Number) row[19]).intValue() : null;
        Long userId = row[20] != null ? ((Number) row[20]).longValue() : null;
        String email = (String) row[21];
        String fullName = (String) row[22];
        String role = (String) row[23];

        AdminPrintingLogResponse response = new AdminPrintingLogResponse(
                printingLogId, adminPrintMail, printerId, printingEndTime, printingStartTime, documentId, fileName,
                fileType, numberOfCopies, pageCount, paperSize, sidedType, printingId, printingAdminMail, expiredTime,
                printerToPrintId, printingStart, studentUploadMail, studentId, numOfPages, userId, email, fullName, role
        );
        return response;
    }

    // Count number of users and page counts by month
    @Query(value = "SELECT YEAR(p.printing_start_time) AS year, " +
            "MONTH(p.printing_start_time) AS month, " +
            "COUNT(DISTINCT pl.student_id) AS user_count, " +
            "SUM(d.page_count) AS total_page_count " +
            "FROM \n" +
            "    printing_log pl\n" +
            "LEFT JOIN \n" +
            "    document d ON pl.document_id = d.id\n" +
            "LEFT JOIN \n" +
            "    printing p ON p.document_id = d.id\n" +
            "GROUP BY YEAR(p.printing_start_time), MONTH(p.printing_start_time)",
            nativeQuery = true)
    List<Object[]> countUsersAndPagesByMonth();

    // Count number of users and page counts by quarter
    @Query(value = "SELECT YEAR(p.printing_start_time) AS year, " +
            "QUARTER(p.printing_start_time) AS quarter, " +
            "COUNT(DISTINCT pl.student_id) AS user_count, " +
            "SUM(d.page_count) AS total_page_count " +
            "FROM \n" +
            "    printing_log pl\n" +
            "LEFT JOIN \n" +
            "    document d ON pl.document_id = d.id\n" +
            "LEFT JOIN \n" +
            "    printing p ON p.document_id = d.id\n" +
            "GROUP BY YEAR(p.printing_start_time), QUARTER(p.printing_start_time)",
            nativeQuery = true)
    List<Object[]> countUsersAndPagesByQuarter();

    // Count number of users and page counts by year
    @Query(value = "SELECT YEAR(p.printing_start_time) AS year, " +
            "COUNT(DISTINCT pl.student_id) AS user_count, " +
            "SUM(d.page_count) AS total_page_count " +
            "FROM \n" +
            "    printing_log pl\n" +
            "LEFT JOIN \n" +
            "    document d ON pl.document_id = d.id\n" +
            "LEFT JOIN \n" +
            "    printing p ON p.document_id = d.id\n" +
            "GROUP BY YEAR(p.printing_start_time)",
            nativeQuery = true)
    List<Object[]> countUsersAndPagesByYear();
}