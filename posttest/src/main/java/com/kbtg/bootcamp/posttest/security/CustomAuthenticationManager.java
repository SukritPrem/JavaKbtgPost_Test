package com.kbtg.bootcamp.posttest.security;

import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserRepository userRepository;


    static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    CustomAuthenticationManager(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<User> user = userRepository.findByuserid(authentication.getName());
        CustomUserDetail customUserDetail = new CustomUserDetail(user.get().getUserId(),user.get().getEncoderpassword());
        List<String> stringList = new ArrayList<>();
        stringList.add(user.get().getRoles());
        customUserDetail.setRoles(stringList);
        if (!passwordEncoder().matches(authentication.getCredentials().toString(), customUserDetail.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(customUserDetail.getUsername(), customUserDetail.getPassword(), customUserDetail.getAuthorities());
    }

}