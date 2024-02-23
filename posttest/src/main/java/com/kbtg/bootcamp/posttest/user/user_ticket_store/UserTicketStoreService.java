package com.kbtg.bootcamp.posttest.user.user_ticket_store;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserService;

import java.util.Optional;

public class UserTicketStoreService {

    private Lottery lottery;
    private User user;

    private UserTicketStoreRepository userTicketStoreRepository;

    private LotteryRepository lotteryRepository;

    private final UserService userService;
    UserTicketStoreService(UserService userService)
    {
        this.userService = userService;
    }
    public Integer updateUserTicketAndLotteryAndReturnUserId(Lottery lottery, User user) throws NotFoundException
    {
        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByUseridAndTicket(user.getUserId(),lottery.getTicket());
        if(userTicketStoreOptional.isEmpty())
        {
            //insert ticket to user_ticket_store;
            userTicketStoreRepository.save(new UserTicketStore(
                    user.getUserId(),
                    lottery.getTicket(),
                    lottery.getAmount(),
                    lottery.getPrice()));
            //update database zero because assume User by all lottery;
            lotteryRepository.updateAmountZeroByticket(lottery.getTicket());

            //save user_action
            return userService.saveUserActionReturnId("BUY",
                    Integer.parseInt(lottery.getAmount()),
                    user,
                    lottery);
        }
        else
        {
            UserTicketStore userLottery = userTicketStoreOptional.get();
            //Update database zero because assume User buy all lottery;
            lotteryRepository.updateAmountZeroByticket(lottery.getTicket());
            Integer totalAmount = Integer.parseInt(userLottery.getAmount()) +
                    Integer.parseInt(lottery.getAmount());
            //Update amount
            userTicketStoreRepository.updateAmountByuserIdAndTicket(Integer.toString(totalAmount),
                    userLottery.getUserid(),
                    lottery.getTicket());
            //save user_action
            userService.saveUserAction("BUY",
                    Integer.parseInt(lottery.getAmount()),
                    user,
                    lottery);
            //save user_action
            return userService.saveUserActionReturnId("UPDATE",
                    Integer.parseInt(lottery.getAmount()),
                    user, lottery);
        }
    }

}
