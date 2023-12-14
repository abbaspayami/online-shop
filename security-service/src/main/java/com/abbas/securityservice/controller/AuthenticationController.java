package com.abbas.securityservice.controller;

import com.abbas.securityservice.dto.AuthenticateRequest;
import com.abbas.securityservice.dto.AuthenticationResponse;
import com.abbas.securityservice.dto.signUpRequest;
import com.abbas.securityservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody signUpRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
