package com.kbtg.bootcamp.posttest.security.JWT;

import com.kbtg.bootcamp.posttest.security.DomainExtractor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final DomainExtractor domainExtractor;
    public JwtAuthFilter(JwtService jwtService,DomainExtractor domainExtractor) {

        this.jwtService = jwtService;
        this.domainExtractor =domainExtractor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String username;
        final String jwtToken;

        System.out.print("Hey Man Verifiy before get in!!!\n");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            System.out.print("Hey Man you already Pass\n");
            filterChain.doFilter(request,response);
            return;
        }
        System.out.print("Come on Come on \n");
        jwtToken = authHeader.substring(7);
        username = jwtService.extractUsername(jwtToken);
        System.out.print("token :" + jwtToken + "\n");
        System.out.print("username :" + username + "\n");
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<GrantedAuthority> authorities = jwtService.getAuthorities(jwtToken);
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtService.SECRET_KEY).parseClaimsJws(jwtToken);

            Map<String, Object> allClaims = claims.getBody(); // Get all claims

// Iterate over all claims and print them
            for (Map.Entry<String, Object> entry : allClaims.entrySet()) {
                String claimKey = entry.getKey();
                Object claimValue = entry.getValue();
                System.out.println("Claim: " + claimKey + ", Value: " + claimValue);
            }
            if (!jwtService.isTokenExpired(jwtToken)) {
                System.out.print("Hello jwtservice is Expired?\n");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }


        filterChain.doFilter(request,response);
    }
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
