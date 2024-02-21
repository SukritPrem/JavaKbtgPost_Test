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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
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

        if(isAuthenticated())
        {
            filterChain.doFilter(request, response);
            return;
        }
        String xAuth = request.getHeader("Authorization");
        System.out.print(xAuth + "\n" + getUsernameFromRequest(request));
        String username = getUsernameFromRequest(request);
        String password = getPasswordFromRequest(request);
        User user = userRepository.findByuserid(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (username != null && password != null) {
            Authentication authentication;
            authentication = new UsernamePasswordAuthenticationToken(username, password);
            try {
                Authentication authenticated = customAuthenticationManager.authenticate(authentication);
                CustomUserDetail customUserDetail = new CustomUserDetail(user.getUserId(),user.getEncoderpassword());
                List<String> stringList = new ArrayList<>();
                stringList.add(user.getRoles());
                System.out.print(user.getRoles());
                customUserDetail.setRoles(stringList);
                JwtService jwtService = new JwtService();
                String token = jwtService.generateToken(customUserDetail);
                response.addHeader("Authorization", "Bearer " + token);
                SecurityContextHolder.getContext().setAuthentication(authenticated);
//                StringBuffer contextPath = request.getRequestURL();
//                System.out.print(contextPath + "Hello\n");
//                handleAuthenticationSuccess(request, response, authenticated);
            } catch (AuthenticationServiceException e) {
                handleAuthenticationFailure(request, response, e);
                return;
            }
        }


//        if (authentication != null && authentication.isAuthenticated() ) {
//            // Retrieve user details from authentication object
//            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
//            JwtService jwtService = new JwtService();
//            // Generate JWT token
//            String token = jwtService.generateToken(userDetails);
//
//            // Set JWT token in the response headers
//            response.addHeader("Authorization", "Bearer " + token);
//        }

        // Continue the filter chain
//        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
    private void handleAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationServiceException e) {
    }

    private void handleAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authenticated) {
        try {
            // Redirecting to a default page after successful authentication
            System.out.print(request.getContextPath() + "\n");
            response.sendRedirect(request.getRequestURI().toString());
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace(); // Or log the exception
        }
    }


    private String getUsernameFromRequest(HttpServletRequest request) {
        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Extract the Base64-encoded credentials part
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            // Decode the Base64 string
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            // Convert the byte array to a string using UTF-8 encoding
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            // Split the string into username and password
            String[] parts = credentials.split(":", 2);
            if (parts.length == 2) {
                return parts[0];
            }
        }
        return null;
    }
    private String getPasswordFromRequest(HttpServletRequest request) {
        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Extract the Base64-encoded credentials part
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            // Decode the Base64 string
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            // Convert the byte array to a string using UTF-8 encoding
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            // Split the string into username and password
            String[] parts = credentials.split(":", 2);
            if (parts.length == 2) {
                return parts[1];
            }
        }
        return null;
    }


//    private final UserRepository userRepository;
//
//    private final JwtService jwtService;
//
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    public CustomBasicAuthenticationFilter(
//            UserRepository userRepository,
//            JwtService jwtService,
//            AuthenticationManager authenticationManager) {
//        this.userRepository = userRepository;
//        this.jwtService = jwtService;
//        this.authenticationManager = authenticationManager;
//    }

//    @Override
//    public void doFilter(
//            ServletRequest request,
//            ServletResponse response,
//            FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        // Extract username and password from the request
////        String username = httpRequest.getHeader("username");
////        String password = httpRequest.getHeader("password");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String xAuth = httpRequest.getHeader("Authorization");
//        System.out.print(xAuth);
//        String username = auth.getName();
//        String password = "";
//        System.out.print(username + " " + password + "\n");
//        User user = userRepository.findByuserid(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
////    if(password == null)
////        throw new BadCredentialsException("User OR Password not found");
//    boolean passwordValid = BCrypt.checkpw(password,user.getEncoderpassword());
//    if (!passwordValid) {
//        throw new BadCredentialsException("Invalid password");
//    }
//        CustomUserDetail userDetail = new CustomUserDetail(user.getUserId(),user.getEncoderpassword());
//        String token = jwtService.generateToken(userDetail);
//        // Set JWT token in response header or cookie
//        Cookie cookie = new Cookie("myCookie", token);
//        // Optionally set cookie attributes such as path, maxAge, etc.
//        cookie.setPath("/"); // set the cookie to be accessible from the entire application
//        cookie.setMaxAge(3600); // set the cookie to expire in 1 hour (in seconds)
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        httpResponse.addHeader("Authorization", "Bearer " + token);
//        long expirationTimeMillis = System.currentTimeMillis() + (60 * 60 * 1000); // 1 hour in milliseconds
//        Date expirationDate = new Date(expirationTimeMillis);
//        httpResponse.addDateHeader("Expires", expirationDate.getTime());
//        httpResponse.sendRedirect("/default-url");
//        chain.doFilter(request, response);
//    }


}
