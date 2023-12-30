package com.abbas.securityservice.controller.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {

}
