package com.abbas.securityservice.controller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record AuthenticationRequest(
        @NotEmpty
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
        String email,
        @NotEmpty(message = "password is required")
        String password) {


}
