package com.abbas.securityservice.config;

import com.abbas.securityservice.repository.UserHistoryRepository;
import com.abbas.securityservice.repository.UserRepository;
import com.abbas.securityservice.service.impl.HashMapLogout;
import com.abbas.securityservice.service.impl.JwtAuthenticationFilter;
import com.abbas.securityservice.service.impl.JwtServiceImpl;
import com.abbas.securityservice.service.impl.RedisLogout;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Log4j2
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtServiceImpl;
    private final UserHistoryRepository userHistoryRepository;

    @Value("${inMemory.store}")
    private String inMemoryStore;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.debug("SecurityConfig: security Filter Chain");
            http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user/**").hasAuthority("ADMIN")
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

    @Bean
    public UserDetailsService userDetailsService(){
        log.debug("SecurityConfig: user Details Service");
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(" User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private LogoutHandler getInMemoryStore() {
        log.debug("SecurityConfig: getting InMemory Store");
        if (inMemoryStore.equalsIgnoreCase("Redis")) {
            return new RedisLogout(jwtServiceImpl, userHistoryRepository);
        } else {
            return new HashMapLogout(jwtServiceImpl, userHistoryRepository);
        }
    }

}
