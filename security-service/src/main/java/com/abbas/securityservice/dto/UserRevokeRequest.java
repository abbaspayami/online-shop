package com.abbas.securityservice.dto;

import lombok.Builder;

@Builder
public record UserRevokeRequest(String email) {
}
