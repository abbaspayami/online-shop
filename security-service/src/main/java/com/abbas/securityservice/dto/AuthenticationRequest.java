package com.abbas.securityservice.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import static com.abbas.securityservice.config.SecurityConstants.EMAIL_REQUIRED;
import static com.abbas.securityservice.config.SecurityConstants.PASSWORD_REQUIRED;

public record AuthenticationRequest(
        @NotEmpty(message = EMAIL_REQUIRED)
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
        String email,
        @NotEmpty(message = PASSWORD_REQUIRED)
        String password) {


}
