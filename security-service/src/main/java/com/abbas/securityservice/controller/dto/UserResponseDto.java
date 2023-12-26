package com.abbas.securityservice.controller.dto;

import com.abbas.securityservice.domain.Role;

public record UserResponseDto(Integer id, String firstname, String lastName, String email, Role role) {

}
