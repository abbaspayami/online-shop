package com.abbas.securityservice.controller.dto;

import com.abbas.securityservice.domain.Role;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Id
    private Integer id;
    private String firstname;
    private String lastName;
    private String email;
    private Role role;

}
