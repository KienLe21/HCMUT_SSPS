package com.hcmut.ssps_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportItem {
    private Integer year;
    private Integer month; // Only for monthly frequency
    private Integer quarter; // Only for quarterly frequency
    private Long userCount;
    private Long totalPageCount;
}