package com.hcmut.ssps_server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY) // Chỉ bao gồm các thuộc tính không null hoặc không trống
public class PrintRequestResponse {
    private List<PrintRequestPartResponse> pendingPrints;
    private List<PrintRequestPartResponse> completedPrints;
}
