package com.hcmut.ssps_server.service.implement;

import com.hcmut.ssps_server.dto.response.AdminRatingResponse;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.exception.AppException;
import com.hcmut.ssps_server.exception.ErrorCode;
import com.hcmut.ssps_server.mapper.AdminRatingMapper;
import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.repository.RatingRepository;
import com.hcmut.ssps_server.repository.UserRepository.StudentRepository;
import com.hcmut.ssps_server.service.interf.IRatingService;
import java.util.*;

import com.hcmut.ssps_server.dto.request.RatingCreationRequest;
import com.hcmut.ssps_server.dto.response.RatingResponse;
import com.hcmut.ssps_server.dto.response.AdminRatingResponse;
import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.model.Printing;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.repository.RatingRepository;
import com.hcmut.ssps_server.repository.PrintingRepository;
import com.hcmut.ssps_server.repository.UserRepository.StudentRepository;
import com.hcmut.ssps_server.service.interf.IRatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RatingService implements IRatingService {
    RatingRepository ratingRepo;
    StudentRepository studentRepo;
    PrintingRepository printingRepo;
    AdminRatingMapper adminRatingMapper;
    @Override
    public Page<AdminRatingResponse> getAllRatings(Pageable pageable) {
        Page<AdminRatingResponse> ratingPage =  ratingRepo.findAll(pageable).map(adminRatingMapper::toAdminRatingResponse);
        if(ratingPage.isEmpty()){
            throw new AppException(ErrorCode.RATING_NOT_FOUND);
        }
        return  ratingPage;
    }

    @Override
    public Page<AdminRatingResponse> getRatingByPrintingId(Long printingId, Pageable pageable) {
        Printing printing = printingRepo.findById(printingId).orElseThrow(() -> new AppException(ErrorCode.PRINT_REQUEST_NOT_FOUND));;
        return ratingRepo.findByPrinting(printing, pageable).map(adminRatingMapper::toAdminRatingResponse);
    }

    @Override
    public Page<AdminRatingResponse> getRatingsByStudentId(Long studentId, Pageable pageable) {
        Student student = studentRepo.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        return ratingRepo.findByStudent(student,pageable).map(adminRatingMapper::toAdminRatingResponse);
    }

    @Override
    public void deleteRating(Long ratingId) {
        if (!ratingRepo.existsById(ratingId)) {
            throw new AppException(ErrorCode.PRINT_REQUEST_NOT_FOUND);
        }
        ratingRepo.deleteById(ratingId);
    }
    @Override
    public Rating createRating(RatingCreationRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        Student student = studentRepo.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Printing printing = printingRepo.findById(request.getPrintingId())
                .orElseThrow(() -> new RuntimeException("Printing not found"));

        Rating rating = new Rating();
        rating.setRating(request.getRating());
        rating.setComment(request.getComment());
        rating.setPrinting(printing);
        rating.setStudent(student);

        return ratingRepo.save(rating);
    }

    @Override
    public List<RatingResponse> getRatingOfCurrentStudent() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        Student student = studentRepo.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Rating> ratings = ratingRepo.findByStudent(student);
        List<RatingResponse> ratingResponses;
        ratingResponses = ratings.stream().map(rating -> {
            RatingResponse response = new RatingResponse();
            response.setId(rating.getId());
            response.setRating(rating.getRating());
            response.setComment(rating.getComment());
            response.setPrintingId((long) rating.getPrinting().getId());
            return response;
        }).collect(Collectors.toList());

        return ratingResponses;
    }

    @Override
    public RatingResponse updateRating(Long ratingId, Map<String, Object> updates) {
        Rating rating = ratingRepo.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Rating.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, rating, value);
            }
        });
        ratingRepo.save(rating);

        RatingResponse response = new RatingResponse();
        response.setId(rating.getId());
        response.setRating(rating.getRating());
        response.setComment(rating.getComment());
        response.setPrintingId((long) rating.getPrinting().getId());

        return response;
    }

    @Override
    public RatingResponse getRatingByRatingId(Long ratingId) {
        Rating rating = ratingRepo.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        RatingResponse response = new RatingResponse();
        response.setId(rating.getId());
        response.setRating(rating.getRating());
        response.setComment(rating.getComment());
        response.setPrintingId((long) rating.getPrinting().getId());

        return response;
    }
}
