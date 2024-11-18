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
            "    p.student_upload_mail, " +
            "    d.id AS document_id, " +
            "    d.file_name, " +
            "    d.number_of_copies, " +
            "    d.page_count, " +
            "    d.paper_size, " +
            "    d.sided_type, " +
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
                    (String) row[3],                                // p.student_upload_mail
                    ((Number) row[4]).longValue(),                  // d.id
                    (String) row[5],                                // d.file_name
                    ((Number) row[6]).intValue(),                   // d.number_of_copies
                    ((Number) row[7]).intValue(),                  // d.page_count
                    (String) row[8],                               // d.paper_size
                    (String) row[9],                               // d.sided_type
                    row[10] != null ? ((Number) row[10]).intValue() : null, // pl.printer_id
                    row[11] != null ? ((Timestamp) row[11]).toLocalDateTime().toLocalDate() : null, // pl.printing_end_time
                    row[12] != null ? ((Timestamp) row[12]).toLocalDateTime().toLocalDate() : null  // pl.printing_start_time
            );
            requestPartResponses.add(response);
        }
        return requestPartResponses;
    }
}
