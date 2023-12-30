package com.abbas.securityservice.service;

import com.abbas.securityservice.dto.AuthenticationRequest;
import com.abbas.securityservice.dto.AuthenticationResponse;
import com.abbas.securityservice.dto.signUpRequest;
import com.abbas.securityservice.entity.User;

public interface AuthenticationService {
    AuthenticationResponse signup(signUpRequest request);

     void saveUserHistory(User user, String jwtToken);

    boolean revokeAllUserTokens(String userEmail);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
