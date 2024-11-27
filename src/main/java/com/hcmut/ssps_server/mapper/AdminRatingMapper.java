package com.hcmut.ssps_server.mapper;

import com.hcmut.ssps_server.dto.response.AdminRatingResponse;
import com.hcmut.ssps_server.model.Rating;
import org.springframework.stereotype.Component;

@Component
public class AdminRatingMapper {
    public AdminRatingResponse toAdminRatingResponse(Rating rating) {
        if (rating == null) {
            return null;
        } else {
            AdminRatingResponse.AdminRatingResponseBuilder adminRatingResponse = AdminRatingResponse.builder();
            adminRatingResponse.id(rating.getId());
            adminRatingResponse.rating(rating.getRating());
            adminRatingResponse.printingLogId((long) rating.getPrintingLog().getId());
            adminRatingResponse.studentName(rating.getStudent().getUser().getFullName());
            adminRatingResponse.studentEmail(rating.getStudent().getUser().getEmail());
            adminRatingResponse.documentId((long) rating.getPrintingLog().getDocument().getId());
            adminRatingResponse.comment(rating.getComment());
            return adminRatingResponse.build();
        }
    }
}
