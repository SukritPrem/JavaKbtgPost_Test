package com.kbtg.bootcamp.posttest.security;

import com.kbtg.bootcamp.posttest.security.JWT.JwtService;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CustomBasicAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;


    private final DomainExtractor domainExtractor;
    private final CustomAuthenticationManager customAuthenticationManager;
    CustomBasicAuthenticationFilter(UserRepository userRepository,
                                    CustomAuthenticationManager customAuthenticationManager,
                                    DomainExtractor domainExtractor)
    {
        this.userRepository = userRepository;
        this.customAuthenticationManager = customAuthenticationManager;
        this.domainExtractor =domainExtractor;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if(isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            authenticateUser(request, response,filterChain);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) throws IOException, InterruptedException, ServletException {

        String userid = getUsernameFromRequest(request);
        String password = getPasswordFromRequest(request);
        if (userid != null && password != null) {
            Optional<User> user = userRepository.findByuserid(userid);
            try {
//                System.out.print("I'm here.\n");
                if(user.isPresent()) {
//                    System.out.print("I'm Present.\n");
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userid, password);

                    Authentication authenticated = customAuthenticationManager.authenticate(authentication);
                    response.addHeader("Authorization", "Bearer " + createJwtToken(user.get()));
                    SecurityContextHolder.getContext().setAuthentication(authenticated);
                }
//                System.out.print("I'm going.\n");
            }
            catch (AuthenticationServiceException e)
            {
                handleAuthenticationFailure(request, response, e);
            }
        }

        filterChain.doFilter(request, response);

    }

    public String createJwtToken(User user){
        CustomUserDetail customUserDetail = new CustomUserDetail(user.getUserId(),user.getEncoderpassword());
        List<String> stringList = new ArrayList<>();
        stringList.add(user.getRoles());
        customUserDetail.setRoles(stringList);
        JwtService jwtService = new JwtService();
        return jwtService.generateToken(customUserDetail);
    }
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    private void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        if (e instanceof AuthenticationServiceException || e instanceof ServletException) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Resource not found");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error occurred");
        }
    }

    private String getUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            if (parts.length == 2) {
                return parts[0];
            }
        }
        return null;
    }
    private String getPasswordFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            if (parts.length == 2) {
                return parts[1];
            }
        }
        return null;
    }





}
