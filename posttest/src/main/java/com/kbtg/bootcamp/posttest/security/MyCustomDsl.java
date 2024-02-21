package com.kbtg.bootcamp.posttest.security;

import com.kbtg.bootcamp.posttest.security.JWT.JwtService;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

    private UserRepository userRepository;

    private JwtService jwtService;
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//        http.addFilter(new CustomBasicAuthenticationFilter(userRepository,jwtService,authenticationManager));
//    }

    public static MyCustomDsl customDsl() {
        return new MyCustomDsl();
    }
}
