package com.abbas.securityservice.controller.dto;

import com.abbas.securityservice.domain.Role;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record signUpRequest(@NotEmpty String firstname,
                            @NotEmpty String lastname,
                            @NotEmpty(message = "Email is required")
                            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
                            String email,
                            @NotEmpty(message = "password is required")
                            String password,
                            @NotNull Role role) {

}
