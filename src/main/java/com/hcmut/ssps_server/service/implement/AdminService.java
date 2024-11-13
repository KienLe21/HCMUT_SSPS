package com.hcmut.ssps_server.service.implement;

import com.hcmut.ssps_server.dto.request.UserCreationRequest;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.dto.response.UserResponse;
import com.hcmut.ssps_server.exception.AppException;
import com.hcmut.ssps_server.exception.ErrorCode;
import com.hcmut.ssps_server.mapper.StudentMapper;
import com.hcmut.ssps_server.mapper.UserMapper;
import com.hcmut.ssps_server.model.Printing;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.model.user.User;

import com.hcmut.ssps_server.repository.PrintingRepository;
import com.hcmut.ssps_server.repository.UserRepository.StudentRepository;
import com.hcmut.ssps_server.repository.UserRepository.UserRepository;
import com.hcmut.ssps_server.service.interf.IAdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminService implements IAdminService {
    UserRepository userRepository;
    UserMapper userMapper;
    StudentRepository studentRepository;
    StudentMapper studentMapper;
    PrintingRepository printingRepository;

    @Override
    public User createAdmin(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setRole("ADMIN");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public StudentResponse getStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return studentMapper.toStudentResponse(student);
    }

    @Override
    public List<StudentResponse> getAllStudents(Pageable pageable) {
        Page<StudentResponse> studentResponsePage = studentRepository.findAllStudents(pageable).map(studentMapper::toStudentResponse);
        List<StudentResponse> studentResponseList = studentResponsePage.getContent();
        return studentResponseList;
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User admin = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(admin);
    }

    @Override
    public String checkExpiredDocument() {
        LocalDateTime now = LocalDateTime.now();
        List<Printing> expired = printingRepository.findByExpiredTimeBefore(now);
        if (expired.isEmpty()) {
            return ("No expired print request found");
        }
        printingRepository.deleteAll(expired);
        return ("Expired print request deleted");
    }
}
