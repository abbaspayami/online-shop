package com.abbas.securityservice.dto;

import com.abbas.securityservice.domain.Role;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.abbas.securityservice.config.SecurityConstants.*;


public record UserRequestDto(@NotEmpty(message = FIRSTNAME_REQUIRED)
                             String firstname,
                             @NotEmpty(message = LASTNAME_REQUIRED)
                             String lastName,
                             @NotEmpty(message = EMAIL_REQUIRED)
                             @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
                             String email,
                             @NotNull Role role) {

}
