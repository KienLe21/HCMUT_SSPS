package com.hcmut.ssps_server.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingResponse {
    int id;
    int rating;
    String comment;
    Long printingLogId;
}
