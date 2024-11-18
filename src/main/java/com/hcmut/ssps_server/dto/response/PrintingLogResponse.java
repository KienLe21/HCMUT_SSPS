package com.hcmut.ssps_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PrintingLogResponse {
    private Integer logId;
    private String documentName;
    private Integer pagesPrinted;
    private LocalDateTime printingStartTime;
    private LocalDateTime printingEndTime;
    private Integer printerToPrintID;
}