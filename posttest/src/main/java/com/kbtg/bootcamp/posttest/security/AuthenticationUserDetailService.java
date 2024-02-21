//package com.kbtg.bootcamp.posttest.security;
//
//
//
//@Service
//public class AuthenticationUserDetailService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//    private final JwtService jwtService;
//
////    @Autowired
////    private PasswordEncoder passwordEncoder;
//    public AuthenticationUserDetailService(UserRepository userRepository, JwtService jwtService) {
//        this.userRepository = userRepository;
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByuserid(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//        System.out.print(user.getEncoderpassword() + user.getRoles());
//        return new CustomUserDetail(user.getUserId(), user.getEncoderpassword());
//    }
//
//    public String generateJwt(String username) throws NotFoundException {
//        User user = userRepository.findByuserid(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//        CustomUserDetail userDetail = new CustomUserDetail(user.getUserId(),user.getEncoderpassword());
//        userDetail.setRoles(List.of(user.getRoles()));
//        return jwtService.generateToken(userDetail);
//    }
//}
