package com.hcmut.ssps_server.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingCreationRequest {

    @NotNull(message = "PRINTING_ID_CANNOT_BE_NULL")
    Long printingId;

    @Min(value = 1, message = "RATING_MIN_VALUE")
    @Max(value = 5, message = "RATING_MAX_VALUE")
    int rating;

    @Size(min = 1, max = 500, message = "COMMENT_INVALID_LENGTH")
    String comment;
}
