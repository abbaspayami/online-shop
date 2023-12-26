package com.abbas.securityservice.controller;

import com.abbas.securityservice.controller.dto.AuthenticationRequest;
import com.abbas.securityservice.controller.dto.AuthenticationResponse;
import com.abbas.securityservice.controller.dto.UserRevokeRequest;
import com.abbas.securityservice.controller.dto.signUpRequest;
import com.abbas.securityservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/revokeAllUserTokens")
    public ResponseEntity<Void> revocation(@RequestBody UserRevokeRequest request) {
        boolean success = authService.revokeAllUserTokens(request.getEmail());
        if (success) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
