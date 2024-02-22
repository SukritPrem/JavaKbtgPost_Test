package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Transactional
    @ResponseBody
    @PostMapping("/{userId}/lotteries/{ticketId}")
    public  Map<String, String>  UserBuyTicket(@PathVariable  @Pattern(regexp = "\\d{10}") String userId,
                                            @PathVariable @Pattern(regexp = "\\d{6}") String ticketId) throws NotFoundException{
            return Map.of(
                    "id", Integer.toString(userService.UserBuyTicket(userId, ticketId))
            );
    }

    @GetMapping("/{userId}/lotteries")
    public ResponseEntity<?> UserGetAllTicket(@PathVariable  @Pattern(regexp = "\\d{10}") String userId) throws NotFoundException {
        return new ResponseEntity<>(
                userService.allTotalTicket(userId),
                HttpStatus.OK
        );
    }

    @ResponseBody
    @DeleteMapping("/{userId}/lotteries/{ticketId}")
    public Map<String, String> UserDeleteTicket(@PathVariable  @Pattern(regexp = "\\d{10}") String userId,
                                              @PathVariable  @Pattern(regexp = "\\d{6}") String ticketId) throws NotFoundException {
        UserTicket userTicket = userService.deleteTicket(userId, ticketId);
        return Map.of(
                "ticket", userTicket.getTicket()
        );
    }
}

