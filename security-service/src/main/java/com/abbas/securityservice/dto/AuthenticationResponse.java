package com.abbas.securityservice.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {

}
