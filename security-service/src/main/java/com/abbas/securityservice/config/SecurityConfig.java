package com.abbas.securityservice.config;

import com.abbas.securityservice.exception.NotFoundException;
import com.abbas.securityservice.repository.UserHistoryRepository;
import com.abbas.securityservice.service.InMemoryStore;
import com.abbas.securityservice.service.JwtService;
import com.abbas.securityservice.service.impl.HashMapLogout;
import com.abbas.securityservice.service.impl.RedisLogout;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@Log4j2
@SuppressWarnings({"unused"})
public class SecurityConfig {

    private final OncePerRequestFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final UserHistoryRepository userHistoryRepository;
    private final InMemoryStore memoryStore;

    public SecurityConfig(@Qualifier("JwtAuthenticationFilter") OncePerRequestFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, JwtService jwtService, UserHistoryRepository userHistoryRepository, InMemoryStore memoryStore) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.userHistoryRepository = userHistoryRepository;
        this.memoryStore = memoryStore;
    }

    @Value("${inMemory.store:Map}")
    private String inMemoryStore;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("SecurityConfig: security Filter Chain");

            http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user/**").hasAuthority("ADMIN")
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                    .sessionManagement(se-> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout(a-> a.logoutUrl("/api/v1/auth/logout")
                            .addLogoutHandler(this.getInMemoryStore())
                            .logoutSuccessHandler((request, response, authentication) ->
                                    SecurityContextHolder.clearContext()));
        return http.build();
    }


    private LogoutHandler getInMemoryStore() {
        log.debug("SecurityConfig: getting InMemory Store");
        if (inMemoryStore.equalsIgnoreCase("Redis")) {
            return new RedisLogout(jwtService, userHistoryRepository);
        } else if (inMemoryStore.equalsIgnoreCase("Map")) {
            return new HashMapLogout(jwtService, userHistoryRepository, memoryStore);
        }else{
            throw new NotFoundException(ErrorMessageConstants.MEMORY_STORE_NOT_FOUND);
        }
    }

}
