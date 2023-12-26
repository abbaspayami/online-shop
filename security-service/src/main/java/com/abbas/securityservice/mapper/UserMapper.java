package com.abbas.securityservice.mapper;

import com.abbas.securityservice.controller.dto.UserDto;
import com.abbas.securityservice.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDto modelToDto(User user);

}
