package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.model.Rating;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRatingService {
    Rating createRating(Rating rating); //Hào
    List<Rating> getAllRatings(Pageable pageable); // Triết
    List<Rating> getRatingOfCurrentStudent(); //Hào
    Rating getRatingByPrintingId(int printingId); // Triết
    List<Rating> getRatingsByStudentId(Long studentId, Pageable pageable); //Triết
    Rating updateRating(Rating rating); //Hào
    void deleteRating(Long ratingId); // Triết
}
