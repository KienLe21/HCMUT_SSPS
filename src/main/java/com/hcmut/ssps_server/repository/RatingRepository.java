package com.hcmut.ssps_server.repository;

import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByStudent(Student student);

}
