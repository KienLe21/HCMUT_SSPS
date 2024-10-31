package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.dto.request.StudentCreationRequest;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.model.user.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IStudentService {
    Student createStudent(StudentCreationRequest request);
    StudentResponse getMyInfo();
    public String uploadDocument(MultipartFile file, int printerId) throws IOException;
}
