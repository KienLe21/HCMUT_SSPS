package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.dto.response.RatingResponse;
import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.dto.request.RatingCreationRequest;

import java.util.List;
import java.util.Map;

public interface IRatingService {
//    Rating createRating(Rating rating); //Hào
//    List<Rating> getAllRatings(); // Triết
    Rating createRating(RatingCreationRequest request);
    List<RatingResponse> getRatingOfCurrentStudent();
    RatingResponse updateRating(Long ratingId, Map<String, Object> updates);
//    List<Rating> getRatingOfCurrentStudent(); //Hào
//    Rating getRatingByPrintingId(int printingId); // Triết
//    List<Rating> getRatingsByStudentId(Long studentId); //Triết
//    Rating updateRating(Rating rating); //Hào
//    void deleteRating(int ratingId); // Triết
}
