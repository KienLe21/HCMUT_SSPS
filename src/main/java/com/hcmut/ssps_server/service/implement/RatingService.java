package com.hcmut.ssps_server.service.implement;

import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.exception.AppException;
import com.hcmut.ssps_server.exception.ErrorCode;
import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.repository.RatingRepository;
import com.hcmut.ssps_server.repository.UserRepository.StudentRepository;
import com.hcmut.ssps_server.service.interf.IRatingService;
import java.util.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RatingService implements IRatingService {
    RatingRepository ratingRepository;
    StudentRepository studentRepository;
    @Override
    public Rating createRating(Rating rating) {
        return null;
    }

    @Override
    public List<Rating> getAllRatings(Pageable pageable) {
        Page<Rating> ratingList =  ratingRepository.findAll(pageable);
        return ratingList.getContent();
    }

    @Override
    public List<Rating> getRatingOfCurrentStudent() {
        return null;
    }

    @Override
    public Rating getRatingByPrintingId(int printingId) {
        return ratingRepository.findById((long) printingId).orElseThrow(()->new AppException(ErrorCode.PRINT_REQUEST_NOT_FOUND));
    }

    @Override
    public List<Rating> getRatingsByStudentId(Long studentId, Pageable pageable) {
        Page<Rating> ratingList =  ratingRepository.findById(studentId, pageable);
        if (ratingList.isEmpty()) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }
        return ratingList.getContent();
    }

    @Override
    public Rating updateRating(Rating rating) {
        return null;
    }

    @Override
    public void deleteRating(Long ratingId) {
        if (!ratingRepository.existsById(ratingId)) {
            throw new AppException(ErrorCode.PRINT_REQUEST_NOT_FOUND);
        }
        ratingRepository.deleteById(ratingId);
    }

}
