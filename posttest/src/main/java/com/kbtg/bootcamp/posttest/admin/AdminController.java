package com.kbtg.bootcamp.posttest.admin;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final LotteryService lotteryService;


    AdminController(LotteryService lotteryService){
        this.lotteryService = lotteryService;
    }

    @Transactional
    @PostMapping("/lotteries")
    public Map<String, String> createLottery(@Valid @RequestBody LotteryRequest lotteryRequest) throws NotFoundException {
        UserTicket userTicket = lotteryService.createNewLotteryByAdmin(lotteryRequest);
        return Map.of("ticket",  userTicket.getTicket());
    }
}
