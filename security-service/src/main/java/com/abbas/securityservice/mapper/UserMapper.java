package com.abbas.securityservice.mapper;

import com.abbas.securityservice.controller.dto.UserRequestDto;
import com.abbas.securityservice.controller.dto.UserResponseDto;
import com.abbas.securityservice.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserResponseDto modelToDto(User user);
    User dtoToModel(UserRequestDto userRequestDto);

}
