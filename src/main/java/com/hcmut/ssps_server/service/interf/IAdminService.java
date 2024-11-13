package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.dto.request.UserCreationRequest;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.dto.response.UserResponse;
import com.hcmut.ssps_server.model.user.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAdminService {
    User createAdmin(UserCreationRequest request);
    StudentResponse getStudent(Long studentId);
    List<StudentResponse> getAllStudents(Pageable pageable);
    UserResponse getMyInfo();
    String checkExpiredDocument();
}
