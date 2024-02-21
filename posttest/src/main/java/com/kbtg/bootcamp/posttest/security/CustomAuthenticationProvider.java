package com.kbtg.bootcamp.posttest.security;

//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//    private final CustomUserDetail customUserDetail;
//    public CustomAuthenticationProvider(CustomUserDetail customUserDetail) {
//        this.customUserDetail = customUserDetail;
//    }
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//        // Perform your custom authentication logic here
//        // Retrieve user details from userDetailsService and validate the credentials
//        // You can throw AuthenticationException if authentication fails
//        // Example: retrieving user details by username from UserDetailsService
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        if (userDetails == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        // Example: validating credentials
//        if (!password.equals(userDetails.getPassword())) {
//            throw new AuthenticationException("Invalid credentials") {};
//        }
//        // Create a fully authenticated Authentication object
//        Authentication authenticated = new UsernamePasswordAuthenticationToken(
//                userDetails, password, userDetails.getAuthorities());
//        return authenticated;
//    }
//    @Override
//    public boolean supports(Class<?> authentication) {
//        // Return true if this AuthenticationProvider supports the provided authentication class
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}
