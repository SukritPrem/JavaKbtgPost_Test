package com.kbtg.bootcamp.posttest.user.userOperation;

import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import org.springframework.stereotype.Component;

@Component
public class UserOperation {

    private Lottery lottery;
    private String action;

    private String totalAmount;
    private User user;
    private UserTicketStore userTicketStore;
    public User getUser() {
        return user;
    }
    public void setAmountAndPriceInUseTicketStore()
    {
        userTicketStore.setAmount(Integer.toString(
                    Integer.parseInt(lottery.getAmount()) +
                    Integer.parseInt(userTicketStore.getAmount()
                    )));

        userTicketStore.setPrice(Integer.toString(
                Integer.parseInt(lottery.getPrice())
        ));
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


}
