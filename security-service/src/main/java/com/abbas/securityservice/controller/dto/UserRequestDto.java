package com.abbas.securityservice.controller.dto;

import com.abbas.securityservice.domain.Role;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public record UserRequestDto(@NotEmpty(message = "firstname is required")
                             String firstname,
                             @NotEmpty(message = "lastname is required")
                             String lastName,
                             @NotEmpty(message = "Email is required")
                             @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
                             String email,
                             @NotNull Role role) {

}
