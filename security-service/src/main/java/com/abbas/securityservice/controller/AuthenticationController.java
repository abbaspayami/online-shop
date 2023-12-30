package com.abbas.securityservice.controller;

import com.abbas.securityservice.dto.AuthenticationRequest;
import com.abbas.securityservice.dto.AuthenticationResponse;
import com.abbas.securityservice.dto.UserRevokeRequest;
import com.abbas.securityservice.dto.signUpRequest;
import com.abbas.securityservice.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody @Valid signUpRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/revokeAllUserTokens")
    public ResponseEntity<Void> revocation(@RequestBody UserRevokeRequest request) {
        boolean success = authService.revokeAllUserTokens(request.email());
        if (success) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
