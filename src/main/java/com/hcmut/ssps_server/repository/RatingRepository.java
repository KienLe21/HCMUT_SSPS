package com.hcmut.ssps_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import  org.springframework.data.jpa.repository.JpaRepository;
import com.hcmut.ssps_server.model.Rating;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findById(Long studentId, Pageable pageable);
}
