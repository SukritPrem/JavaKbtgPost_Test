package com.kbtg.bootcamp.posttest.admin;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final LotteryService lotteryService;


    AdminController(LotteryService lotteryService){
        this.lotteryService = lotteryService;
    }

    @Transactional
    @PostMapping("/lotteries")
    public ResponseEntity<?> createLottery(@Valid @RequestBody LotteryRequest lotteryRequest) throws NotFoundException {
        return lotteryService.createNewLottery(lotteryRequest);
    }
}
