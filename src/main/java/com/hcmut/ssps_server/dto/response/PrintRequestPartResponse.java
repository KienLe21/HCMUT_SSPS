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
public class PrintRequestPartResponse {
    private Integer printingId; // p.id
    private String printingAdminMail; // p.admin_print_mail
    private LocalDate expiredTime; // p.expired_time
    private Integer printerToPrintId; // p.printer_to_printid
    private LocalDate printingStart; // p.printing_start_time
    private String studentUploadMail; // p.student_upload_mail
    private Long documentId; // d.id
    private String fileName; // d.file_name
    private String fileType; // d.file_type
    private Integer numberOfCopies; // d.number_of_copies
    private Integer pageCount; // d.page_count
    private String paperSize; // d.paper_size
    private String sidedType; // d.sided_type
    private Long printingLogId; // pl.id
    private String adminPrintMail; // pl.admin_print_mail
    private Integer printerId; // pl.printer_id
    private LocalDate printingEndTime; // pl.printing_end_time
    private LocalDate printingStartTime; // pl.printing_start_time
}