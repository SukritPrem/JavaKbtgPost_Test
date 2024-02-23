package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.UserService;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotteryService {

    private LotteryRepository lotteryRepository;

    private UserRepository userRepository;

    private UserService userService;
    private UserTicketRepository userTicketRepository;

    public LotteryService(LotteryRepository lotteryRepository,
                          UserTicketRepository userTicketRepository,
                          UserService userService) {
        this.lotteryRepository = lotteryRepository;
        this.userTicketRepository = userTicketRepository;
        this.userService = userService;
    }

    public List<String> getAll_lottery() {
        List<Lottery> lotteryList = lotteryRepository.findAll();
        return lotteryList.stream()
                .filter(lottery -> {
                    int amount = Integer.parseInt(lottery.getAmount());
                    return amount > 0;
                })
                .map(Lottery::getTicket)
                .collect(Collectors.toList());
    }

    public UserTicket createNewLotteryByAdmin(LotteryRequest lotteryRequest) throws NotFoundException {
        Optional<Lottery> lotteryOptional = lotteryRepository.findByTicket(lotteryRequest.getTicket());
        Lottery newLottery = new Lottery();
        newLottery.setTicket(lotteryRequest.getTicket());
        newLottery.setPrice(Integer.toString(lotteryRequest.getPrice()));
        newLottery.setAmount(Integer.toString(lotteryRequest.getAmount()));
        //assume admin have 1 person
        Optional<User> user = userRepository.findByroles("ADMIN");

        if(lotteryOptional.isEmpty())
        {
            return CreateNewLotteryAndReturnUserticket(lotteryRepository,
                    userService,
                    newLottery,
                    user.get());
        }
        else
        {
            Lottery lottery = lotteryOptional.get();

            //update lottery
            //update to table lottery
            String totalAmountString = Integer.toString(Integer.parseInt(lottery.getAmount()) + Integer.parseInt(newLottery.getAmount()));
            lotteryRepository.updateAmountByticket(totalAmountString,lottery.getTicket());
            lotteryRepository.updatePriceByticket(lottery.getPrice(),lottery.getTicket());

            //Save user_ticket
            return userService.saveUserActionReturnUserTicket(
                    "ADD",
                    Integer.parseInt(totalAmountString),
                    user.get(),
                    lottery
            );
        }
    }

    public UserTicket CreateNewLotteryAndReturnUserticket(LotteryRepository lotteryRepository,
                                                          UserService userService,
                                                          Lottery newLottery,
                                                          User user)
    {
        lotteryRepository.save(newLottery);
        //save user_action
        return userService.saveUserActionReturnUserTicket(
                "ADD",
                Integer.parseInt(newLottery.getAmount()),
                user,
                newLottery
        );
    }


}