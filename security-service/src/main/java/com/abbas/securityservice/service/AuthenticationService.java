package com.abbas.securityservice.service;

import com.abbas.securityservice.controller.dto.AuthenticationRequest;
import com.abbas.securityservice.controller.dto.AuthenticationResponse;
import com.abbas.securityservice.controller.dto.signUpRequest;
import com.abbas.securityservice.domain.entity.User;

public interface AuthenticationService {
    AuthenticationResponse signup(signUpRequest request);

     void saveUserHistory(User user, String jwtToken);

    boolean revokeAllUserTokens(String userEmail);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
