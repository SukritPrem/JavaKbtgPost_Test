package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.exception.ServerInternalErrorException;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.UserService;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotteryService {

    private LotteryRepository lotteryRepository;

    private UserRepository userRepository;

    private UserService userService;

    private UserTicketStoreService userTicketStoreService;

    public LotteryService(LotteryRepository lotteryRepository,
                          UserService userService,
                          UserRepository userRepository,
                          UserTicketStoreService userTicketStoreService) {
        this.lotteryRepository = lotteryRepository;
        this.userService = userService;
        //for find admin
        this.userTicketStoreService = userTicketStoreService;
        this.userRepository = userRepository;
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

    public UserTicket createNewLotteryByAdmin(LotteryRequest lotteryRequest) throws ServerInternalErrorException {
        try {
            Optional<Lottery> lotteryOptional = lotteryRepository.findByTicket(lotteryRequest.getTicket());
            Lottery newLottery = new Lottery(
                    Integer.toString(lotteryRequest.getPrice()),
                    lotteryRequest.getTicket(),
                    Integer.toString(lotteryRequest.getAmount())
            );
            //assume admin have 1 person
            Optional<User> user = userRepository.findByroles("ADMIN");

            if (lotteryOptional.isEmpty())
                lotteryRepository.save(newLottery);
            else {
                //old lottery
                Lottery lottery = lotteryOptional.get();
                //update price
                if(!newLottery.getPrice().equals(lottery.getPrice()))
                    lottery.setPrice(newLottery.getPrice());
                //update lottery
                //update to table lottery
                String totalAmountString = Integer.toString(Integer.parseInt(lottery.getAmount()) + Integer.parseInt(newLottery.getAmount()));
                lotteryRepository.updateAmountAndPriceByticket(
                        totalAmountString,
                        lottery.getPrice(),
                        lottery.getTicket()
                );
                //update when lottery price changed in userTicketStore
                userTicketStoreService.checkIfPriceLotteryChangeUpdate(lottery.getTicket(),lottery.getPrice());
            }
            return userService.saveUserActionReturnUserTicket(
                    "ADD",
                    newLottery.getAmount(),
                    user.get(),
                    newLottery
            );
        } catch (Exception e) {
            throw new ServerInternalErrorException("Error in Create NewLottery By admin");
        }
    }

//    public UserTicket CreateNewLotteryAndReturnUserticket(LotteryRepository lotteryRepository,
//                                                          UserService userService,
//                                                          Lottery newLottery,
//                                                          User user)
//    {
//        lotteryRepository.save(newLottery);
//        //save user_action
//        return userService.saveUserActionReturnUserTicket(
//                "ADD",
//                Integer.parseInt(newLottery.getAmount()),
//                user,
//                newLottery
//        );
//    }


}