package com.abbas.securityservice.controller.dto;

import com.abbas.securityservice.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class signUpRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}
