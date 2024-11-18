package com.hcmut.ssps_server.repository;

import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.model.user.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import  org.springframework.data.jpa.repository.JpaRepository;
import com.hcmut.ssps_server.model.Rating;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findById(Long studentId, Pageable pageable);
    List<Rating> findByStudent(Student student);
}
