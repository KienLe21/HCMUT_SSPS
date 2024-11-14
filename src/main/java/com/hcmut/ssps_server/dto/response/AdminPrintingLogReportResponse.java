package com.hcmut.ssps_server.dto.response;

import com.hcmut.ssps_server.enums.Frequency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPrintingLogReportResponse {
    private Frequency frequency;
    private List<ReportItem> items;
}
