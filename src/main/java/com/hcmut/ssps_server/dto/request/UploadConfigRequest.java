package com.hcmut.ssps_server.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UploadConfigRequest {
    @NotNull(message = "Printer ID is required")
    int printerId;

    @NotNull(message = "Paper size is required")
    String paperSize;

    @NotNull(message = "Sided type is required")
    String sidedType;

    @Min(value = 1, message = "Number of copies must be at least 1")
    int numberOfCopies;

    @NotNull(message = "Color printed is required")
    boolean isColorPrint;
}
