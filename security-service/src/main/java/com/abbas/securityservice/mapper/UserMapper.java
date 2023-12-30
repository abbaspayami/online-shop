package com.abbas.securityservice.mapper;

import com.abbas.securityservice.dto.UserRequestDto;
import com.abbas.securityservice.dto.UserResponseDto;
import com.abbas.securityservice.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDto modelToDto(User user);
    User dtoToModel(UserRequestDto userRequestDto);

}
