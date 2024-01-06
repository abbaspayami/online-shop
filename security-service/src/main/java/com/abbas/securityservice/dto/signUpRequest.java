package com.abbas.securityservice.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import static com.abbas.securityservice.config.ValidationMessageConstants.EMAIL_REQUIRED;
import static com.abbas.securityservice.config.ValidationMessageConstants.PASSWORD_REQUIRED;

public record signUpRequest(@NotEmpty String firstname,
                            @NotEmpty String lastname,
                            @NotEmpty(message = EMAIL_REQUIRED)
                            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = EMAIL_REQUIRED)
                            String email,
                            @NotEmpty(message = PASSWORD_REQUIRED)
                            String password) {

}
