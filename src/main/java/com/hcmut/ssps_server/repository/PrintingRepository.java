package com.hcmut.ssps_server.repository;

import com.hcmut.ssps_server.dto.response.PrintRequestPartResponse;
import com.hcmut.ssps_server.model.Printing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;

public interface PrintingRepository extends JpaRepository<Printing, Long> {
    List<Printing> findByPrinterToPrintID(int printerToPrintID);
    List<Printing> findByExpiredTimeBefore(LocalDateTime currentTime);

    Page<Printing> findAllByPrinterToPrintIDAndExpiredTimeIsNull(int printerToPrintID, Pageable pageable);

    @Query(value = "SELECT " +
            "    p.id AS printing_id, " +
            "    p.admin_print_mail AS printing_admin_mail, " +
            "    p.expired_time, " +
            "    p.printer_to_printid, " +
            "    p.printing_start_time AS printing_start, " +
            "    p.student_upload_mail, " +
            "    d.id AS document_id, " +
            "    d.file_name, " +
            "    d.file_type, " +
            "    d.number_of_copies, " +
            "    d.page_count, " +
            "    d.paper_size, " +
            "    d.sided_type, " +
            "    pl.id AS printing_log_id, " +
            "    pl.admin_print_mail, " +
            "    pl.printer_id, " +
            "    pl.printing_end_time, " +
            "    pl.printing_start_time " +
            "FROM " +
            "    printing p " +
            "LEFT JOIN " +
            "    document d ON p.document_id = d.id " +
            "LEFT JOIN " +
            "    printing_log pl ON pl.document_id = d.id " +
            "WHERE p.student_upload_mail = :currentEmail",
            nativeQuery = true)
    List<Object[]> viewPrintByCurrentEmailRaw(
            @Param("currentEmail") String currentEmail
    );
    default List<PrintRequestPartResponse> viewPrintByCurrentEmail(String currentEmail) {
        List<Object[]> rawData = viewPrintByCurrentEmailRaw(currentEmail);
        List<PrintRequestPartResponse> requestPartResponses = new ArrayList<>();
        for (Object[] row : rawData) {
            PrintRequestPartResponse response = new PrintRequestPartResponse(
                    ((Number) row[0]).intValue(),                    // p.id
                    (String) row[1],                                // p.admin_print_mail
                    row[2] != null ? ((Timestamp) row[2]).toLocalDateTime().toLocalDate() : null, // p.expired_time
                    ((Number) row[3]).intValue(),                   // p.printer_to_printid
                    row[4] != null ? ((Timestamp) row[4]).toLocalDateTime().toLocalDate() : null, // p.printing_start_time
                    (String) row[5],                                // p.student_upload_mail
                    ((Number) row[6]).longValue(),                  // d.id
                    (String) row[7],                                // d.file_name
                    (String) row[8],                                // d.file_type
                    ((Number) row[9]).intValue(),                   // d.number_of_copies
                    ((Number) row[10]).intValue(),                  // d.page_count
                    (String) row[11],                               // d.paper_size
                    (String) row[12],                               // d.sided_type
                    row[13] != null ? ((Number) row[13]).longValue() : null, // pl.id
                    (String) row[14],                               // pl.admin_print_mail
                    row[15] != null ? ((Number) row[15]).intValue() : null, // pl.printer_id
                    row[16] != null ? ((Timestamp) row[16]).toLocalDateTime().toLocalDate() : null, // pl.printing_end_time
                    row[17] != null ? ((Timestamp) row[17]).toLocalDateTime().toLocalDate() : null  // pl.printing_start_time
            );
            requestPartResponses.add(response);
        }
        return requestPartResponses;
    }
}
