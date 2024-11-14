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
            "    d.number_of_copies, " +
            "    d.page_count, " +
            "    d.paper_size, " +
            "    d.sided_type, " +
            "    p.expired_time, " +
            "    s.student_id, " +
            "    s.num_of_pages, " +
            "    u.email " +
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
            Integer numberOfCopies = ((Number) row[7]).intValue();
            Integer pageCount = ((Number) row[8]).intValue();
            String paperSize = (String) row[9];
            String sidedType = (String) row[10];
            LocalDate expiredTime = row[11] != null ? ((Timestamp) row[11]).toLocalDateTime().toLocalDate() : null;
            Long studentId = row[12] != null ? ((Number) row[12]).longValue() : null;
            Integer numOfPages = row[13] != null ? ((Number) row[13]).intValue() : null;
            String email = (String) row[14];

            AdminPrintingLogResponse response = new AdminPrintingLogResponse(
                    printingLogId, adminPrintMail, printerId, printingEndTime, printingStartTime, documentId, fileName,
                    numberOfCopies, pageCount, paperSize, sidedType, expiredTime,
                    studentId, numOfPages, email
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
            "    d.number_of_copies,\n" +
            "    d.page_count,\n" +
            "    d.paper_size,\n" +
            "    d.sided_type,\n" +
            "    p.expired_time,\n" +
            "    s.student_id,\n" +
            "    s.num_of_pages,\n" +
            "    u.email \n" +
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
        Integer numberOfCopies = ((Number) row[7]).intValue();
        Integer pageCount = ((Number) row[8]).intValue();
        String paperSize = (String) row[9];
        String sidedType = (String) row[10];
        LocalDate expiredTime = row[11] != null ? ((Timestamp) row[11]).toLocalDateTime().toLocalDate() : null;
        Long studentId = row[12] != null ? ((Number) row[12]).longValue() : null;
        Integer numOfPages = row[13] != null ? ((Number) row[13]).intValue() : null;
        String email = (String) row[14];

        AdminPrintingLogResponse response = new AdminPrintingLogResponse(
                printingLogId, adminPrintMail, printerId, printingEndTime, printingStartTime, documentId, fileName,
                numberOfCopies, pageCount, paperSize, sidedType, expiredTime,
                studentId, numOfPages, email
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