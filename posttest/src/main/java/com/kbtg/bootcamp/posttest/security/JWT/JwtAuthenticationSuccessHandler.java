//package com.kbtg.bootcamp.posttest.security.JWT;
//
//import com.kbtg.bootcamp.posttest.security.CustomUserDetail;
//import io.jsonwebtoken.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    @Autowired
//    private JwtService jwtTokenProvider;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        CustomUserDetail user = new CustomUserDetail(authentication.getName(),encoder.encode("password"));
//        String token = jwtTokenProvider.generateToken(user);
//
//        // Add JWT token to the response as a cookie
//        response.addHeader("Authorization", "Bearer " + token);
//
////         Redirect the user or send a response indicating successful authentication
//        try {
//            response.sendRedirect("/default-url");
//        } catch (java.io.IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
//
