package com.hcmut.ssps_server.service.interf;

import com.hcmut.ssps_server.dto.response.PrintRequestResponse;
import com.hcmut.ssps_server.dto.request.StudentCreationRequest;
import com.hcmut.ssps_server.dto.request.UploadConfigRequest;
import com.hcmut.ssps_server.dto.response.PrintingLogResponse;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.model.user.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IStudentService {
    Student createStudent(StudentCreationRequest request);
    StudentResponse getMyInfo();
    String uploadDocument(MultipartFile file, UploadConfigRequest uploadConfig) throws IOException;
    Integer checkRemainingPages() ;
    Integer recharge(int amount);
    List<PrintingLogResponse> getPrintingLogsForStudent();
    String confirm(Long printingId);
    PrintRequestResponse getPrintRequests();
}
