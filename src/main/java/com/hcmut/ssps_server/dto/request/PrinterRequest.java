package com.hcmut.ssps_server.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrinterRequest {
    @Size(min = 1, message = "Document type is required")
    List<String> requiredDocumentType;
    int page = 0;
    int size = 3;
}
