package com.abbas.securityservice.dto;

import com.abbas.securityservice.domain.Role;

public record UserResponseDto(Integer id, String firstname, String lastname, String email, Role role) {

}