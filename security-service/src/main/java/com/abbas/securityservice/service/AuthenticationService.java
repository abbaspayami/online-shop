package com.abbas.securityservice.service;


import com.abbas.securityservice.controller.dto.AuthenticationRequest;
import com.abbas.securityservice.controller.dto.AuthenticationResponse;
import com.abbas.securityservice.controller.dto.signUpRequest;
import com.abbas.securityservice.domain.entity.User;
import com.abbas.securityservice.domain.entity.UserHistory;
import com.abbas.securityservice.repository.UserHistoryRepository;
import com.abbas.securityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse signup(signUpRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        User savedToken = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserHistory(savedToken, jwtService.extractId(jwtToken));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserHistory(User user, String jwtToken) {
        var token = UserHistory.builder()
                .user(user)
                .tokenId(jwtToken)
                .revoked(false)
                .build();
        userHistoryRepository.save(token);
    }

    public boolean revokeAllUserTokens(String userEmail) {
        List<UserHistory> allValidTokensByUser = userHistoryRepository.findAllValidTokensByUser(userEmail);
        if (allValidTokensByUser.isEmpty())
            return false;
        allValidTokensByUser.forEach(t -> {
            t.setRevoked(true);
        });
        userHistoryRepository.saveAll(allValidTokensByUser);
        return true;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user.getEmail());
        saveUserHistory(user, jwtService.extractId(jwtToken));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
