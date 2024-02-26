package com.kbtg.bootcamp.posttest.security;

import com.kbtg.bootcamp.posttest.security.JWT.JwtAuthFilter;
import com.kbtg.bootcamp.posttest.security.JWT.JwtService;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig{

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final JwtAuthFilter jwtAuthFilter;

    private final DomainExtractor domainExtractor;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          DomainExtractor domainExtractor,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationManager = authenticationManager;
        this.domainExtractor = domainExtractor;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers("/admin").hasAnyRole("ADMIN")
                                .requestMatchers("/lotteries").permitAll()
                                .requestMatchers("/users/**").permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(new CustomBasicAuthenticationFilter(userRepository,new CustomAuthenticationManager(userRepository),domainExtractor), JwtAuthFilter.class)
                .build();

    }


}
