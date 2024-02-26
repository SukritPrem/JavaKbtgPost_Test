package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.exception.NotValidExceptionCustom;
import com.kbtg.bootcamp.posttest.exception.Status200Exception;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
       this.userService = userService;
    }



    @Transactional
    @ResponseBody
    @PostMapping("/{userId}/lotteries/{ticketId}")
    public  Map<String, String>  UserBuyTicket(@PathVariable @Pattern(regexp = "\\d{10}", message = "user id need 10 character") String userId,
                                            @PathVariable @Pattern(regexp = "\\d{6}", message = "ticket need 6 character") String ticketId) throws NotFoundException, Status200Exception, NotValidExceptionCustom {
            return Map.of(
                    "id", Integer.toString(userService.userBuyTicket(userId, ticketId))
            );
    }

    @GetMapping("/{userId}/lotteries")
    public ReturnResultAllToUser UserGetAllTicket(@PathVariable  @Pattern(regexp = "\\d{10}",  message = "user id need 10 character") String userId) throws NotFoundException {
        return userService.allTotalTicket(userId);
    }

    @ResponseBody
    @DeleteMapping("/{userId}/lotteries/{ticketId}")
    public Map<String, String> UserDeleteTicket(@PathVariable  @Pattern(regexp = "\\d{10}",message = "user id need 10 character") String userId,
                                              @PathVariable  @Pattern(regexp = "\\d{6}", message = "ticket need 6 character") String ticketId) throws NotFoundException {
        return Map.of(
                "ticket", userService.deleteTicket(userId, ticketId)
        );
    }


}

