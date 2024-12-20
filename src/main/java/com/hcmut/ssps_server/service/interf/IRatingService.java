package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.dto.response.AdminRatingResponse;
import com.hcmut.ssps_server.dto.response.RatingResponse;
import com.hcmut.ssps_server.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.hcmut.ssps_server.dto.request.RatingCreationRequest;

import java.util.List;
import java.util.Map;

public interface IRatingService {
    Rating createRating(RatingCreationRequest request);
    List<RatingResponse> getRatingOfCurrentStudent();
    RatingResponse updateRating(Long ratingId, Map<String, Object> updates);
    RatingResponse getRatingByRatingId(Long ratingId);
    Page<AdminRatingResponse> getAllRatings(Pageable pageable); // Triết
    Page<AdminRatingResponse> getRatingByPrintingLogId(Long printingLogId, Pageable pageable); // Triết
    Page<AdminRatingResponse> getRatingsByStudentId(Long studentId, Pageable pageable); //Triết
    void deleteRating(Long ratingId); // Triết
}
