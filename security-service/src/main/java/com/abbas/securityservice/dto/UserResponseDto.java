package com.abbas.securityservice.dto;

import com.abbas.securityservice.model.RoleEnum;

public record UserResponseDto(Integer id, String firstname, String lastname, String email, RoleEnum roleEnum) {

}
