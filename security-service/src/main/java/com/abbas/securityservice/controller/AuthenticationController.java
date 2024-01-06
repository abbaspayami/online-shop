package com.abbas.securityservice.controller;

import com.abbas.securityservice.dto.AuthenticationRequest;
import com.abbas.securityservice.dto.AuthenticationResponse;
import com.abbas.securityservice.dto.UserRevokeRequest;
import com.abbas.securityservice.dto.signUpRequest;
import com.abbas.securityservice.service.impl.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@SuppressWarnings({"unused"})
@Log4j2
public class AuthenticationController {

    private final JwtAuthenticationService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody @Valid signUpRequest request) {
        log.debug("AuthenticationController: starting signup");
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.debug("AuthenticationController: authenticate");
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/revokeAllUserTokens")
    public ResponseEntity<Void> revocation(@RequestBody UserRevokeRequest request) {
        log.debug("AuthenticationController: revocation");
        boolean success = authService.revokeAllUserTokens(request.email());
        if (success) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
