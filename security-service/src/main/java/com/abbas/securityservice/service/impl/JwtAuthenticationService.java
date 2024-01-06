package com.abbas.securityservice.service.impl;


import com.abbas.securityservice.dto.AuthenticationRequest;
import com.abbas.securityservice.dto.AuthenticationResponse;
import com.abbas.securityservice.dto.signUpRequest;
import com.abbas.securityservice.entity.User;
import com.abbas.securityservice.entity.UserHistory;
import com.abbas.securityservice.enums.Role;
import com.abbas.securityservice.repository.UserHistoryRepository;
import com.abbas.securityservice.repository.UserRepository;
import com.abbas.securityservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationService implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authManager;

    public AuthenticationResponse signup(signUpRequest request) {
        log.debug("starting signup method");
        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        User savedToken = userRepository.save(user);
        var jwtToken = jwtServiceImpl.generateToken(user);
        saveUserHistory(savedToken, jwtServiceImpl.extractId(jwtToken));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void saveUserHistory(User user, String jwtToken) {
        log.debug("saving user history");
        var token = UserHistory.builder()
                .user(user)
                .tokenId(jwtToken)
                .revoked(false)
                .build();
        userHistoryRepository.save(token);
    }

    public boolean revokeAllUserTokens(String userEmail) {
        log.debug("revoking all user token");
        List<UserHistory> allValidTokensByUser = userHistoryRepository.findAllValidTokensByUser(userEmail);
        if (allValidTokensByUser.isEmpty())
            return false;
        allValidTokensByUser.forEach(t -> t.setRevoked(true));
        userHistoryRepository.saveAll(allValidTokensByUser);
        return true;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("authenticate method");
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtServiceImpl.generateToken(user);
        revokeAllUserTokens(user.getEmail());
        saveUserHistory(user, jwtServiceImpl.extractId(jwtToken));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
