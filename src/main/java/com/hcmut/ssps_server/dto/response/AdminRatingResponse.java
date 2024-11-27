package com.hcmut.ssps_server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminRatingResponse {
    int id;
    int rating;
    String comment;
    Long printingLogId;
    Long documentId;
    String studentName;
    String studentEmail;


}
