package com.hcmut.ssps_server.mapper;

import com.hcmut.ssps_server.dto.request.StudentCreationRequest;
import com.hcmut.ssps_server.dto.request.UserCreationRequest;
import com.hcmut.ssps_server.dto.request.UserUpdateRequest;
import com.hcmut.ssps_server.dto.response.StudentResponse;
import com.hcmut.ssps_server.dto.response.UserResponse;
import com.hcmut.ssps_server.model.user.Student;
import com.hcmut.ssps_server.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    User toUser(StudentCreationRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
