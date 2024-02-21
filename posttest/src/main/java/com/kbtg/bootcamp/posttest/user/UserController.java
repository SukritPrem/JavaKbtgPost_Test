package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("")
//    public List<User> getUser(){
//        return this.userService.getUser();
//    }

    @Transactional
    @PostMapping("/{userId}/lotteries/{ticketId}")
    public ResponseEntity<?> UserBuyTicket(@PathVariable  @Pattern(regexp = "\\d{10}") String userId,
                                            @PathVariable @Pattern(regexp = "\\d{6}") String ticketId) throws NotFoundException
    {
        return userService.UserBuyTicket(userId,ticketId);
    }

    @GetMapping("/{userId}/lotteries")
    public ResponseEntity<?> UserGetAllTicket(@PathVariable  @Pattern(regexp = "\\d{10}") String userId)
    {
        return userService.allTotalTicket(userId);
    }

    @DeleteMapping("/{userId}/lotteries/{ticketId}")
    public ResponseEntity<?> UserDeleteTicket(@PathVariable  @Pattern(regexp = "\\d{10}") String userId,
                                              @PathVariable  @Pattern(regexp = "\\d{6}") String ticketId) throws NotFoundException {
        return userService.deleteTicket(userId, ticketId);
    }
}

