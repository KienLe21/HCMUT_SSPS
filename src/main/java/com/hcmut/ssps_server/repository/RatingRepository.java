package com.hcmut.ssps_server.repository;

import com.hcmut.ssps_server.model.Printing;
import com.hcmut.ssps_server.model.Rating;
import com.hcmut.ssps_server.model.user.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import  org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByStudent(Student student);
    Page<Rating> findByStudent(Student student, Pageable pageable);
    Page<Rating> findByPrinting(Printing printing, Pageable pageable);
}
