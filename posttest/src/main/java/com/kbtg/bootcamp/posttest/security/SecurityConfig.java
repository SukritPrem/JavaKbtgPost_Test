package com.kbtg.bootcamp.posttest.security;

import com.kbtg.bootcamp.posttest.security.JWT.JwtAuthFilter;
import com.kbtg.bootcamp.posttest.security.JWT.JwtService;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig{

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final AuthenticationUserDetailService authenticationUserDetailService;
    private final JwtAuthFilter jwtAuthFilter;

    private final DomainExtractor domainExtractor;
    private final AuthenticationManager authenticationManager;
//    private final ApplicationConfig applicationConfig;
//    private final CustomBasicAuthenticationFilter customBasicAuthenticationFilter;
//private final CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    public SecurityConfig(AuthenticationUserDetailService authenticationUserDetailService,
                          JwtAuthFilter jwtAuthFilter,
//                            CustomAuthenticationProvider customAuthenticationProvider,
//                          ApplicationConfig applicationConfig,
//                          CustomBasicAuthenticationFilter customBasicAuthenticationFilter,
                          DomainExtractor domainExtractor,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository) {
//        this.applicationConfig = applicationConfig;
        this.authenticationUserDetailService = authenticationUserDetailService;
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationManager = authenticationManager;
//        this.customAuthenticationProvider = customAuthenticationProvider;
//        this.customBasicAuthenticationFilter =customBasicAuthenticationFilter;
        this.domainExtractor = domainExtractor;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers("/admin").hasAnyRole("ADMIN")
                                .requestMatchers("/lotteries").permitAll()
                                .requestMatchers("/users").permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(new CustomBasicAuthenticationFilter(userRepository,new CustomAuthenticationManager(userRepository),domainExtractor), JwtAuthFilter.class);
//                .httpBasic(Customizer.withDefaults()); // Add your custom authentication filter
//                        .usernameParameter("username") // Customize the username parameter
//                        .passwordParameter("password") // Customize the password parameter
//                        .loginProcessingUrl("/login") // Specify the login processing URL
//                        .successHandler(customAuthenticationSuccessHandler()) // Customize success handler if needed
//                        .failureHandler(customAuthenticationFailureHandler()); // Customize failure handler if needed)



        return http.build();
    }
//                .addFilterAfter(
//                        new CustomBasicAuthenticationFilter(userRepository,jwtService), BasicAuthenticationFilter.class)
    //                .addFilter(customBasicAuthenticationFilter)

//    @Bean
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(customAuthenticationProvider);
//    }

//
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authenticationUserDetailService);
        authenticationProvider.setPasswordEncoder(CustomAuthenticationManager.passwordEncoder());
        System.out.print(authenticationProvider + "Hello");
        return authenticationProvider;
    }
//

//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder authBuilder,
//                                                   UserDetailsService userDetailsService,
//                                                   PasswordEncoder passwordEncoder) throws Exception {
//    authBuilder
//            .userDetailsService(userDetailsService)
//            .passwordEncoder(passwordEncoder);
//    return authBuilder.build();
//}

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
