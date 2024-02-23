package com.kbtg.bootcamp.posttest.user.userOperationService;

import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import org.springframework.stereotype.Service;

@Service
public class UserOperationsService {

    private Lottery lottery;
    private String action;

    private User user;
    private UserTicketStore userTicketStore;
    public User getUser() {
        return user;
    }
    public void setAmountInUseTicketStore()
    {
        userTicketStore.setAmount(Integer.toString(
                    Integer.parseInt(lottery.getAmount()) +
                    Integer.parseInt(userTicketStore.getAmount()
                    )));
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Lottery getLottery() {
        return lottery;
    }

    public void setLottery(Lottery lottery) {
        this.lottery = lottery;
    }


    public UserTicketStore getUserTicketStore() {
        return userTicketStore;
    }

    public void setUserTicketStore(UserTicketStore userTicketStore) {
        this.userTicketStore = userTicketStore;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


//    private UserService userService;
//    private UserTicketStoreService userTicketStoreService;

//    public UserOperationsService(UserService userService,
//                                 UserTicketStoreService userTicketStoreService)
//    {
//        this.userService =userService;
//        this.userTicketStoreService = userTicketStoreService;
//    }
//
//    public Integer UserBuyTicket(String userId,String ticketId) throws NotFoundException{
//        if(userService.CheckUserAndLottery(userId,ticketId)) {
//            Map<String, Object> twoclass = userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userId, ticketId);
//            userService.
//        }
//        else
//            throw new NotFoundException("Error user id or lottery not found");
//    }
}
