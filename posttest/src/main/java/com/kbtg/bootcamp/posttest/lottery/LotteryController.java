package com.kbtg.bootcamp.posttest.lottery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lotteries")
public class LotteryController {

    @Autowired
    private LotteryService lotteryService;


    public LotteryController(LotteryService lotteryService)
    {
        this.lotteryService = lotteryService;
    }


    @GetMapping("")
    @ResponseBody
    public Map<String, List<String>> getData() {
        Map<String, List<String>> data = new HashMap<>();
        data.put("ticket", lotteryService.getAll_lottery());
        return data;
    }
}

