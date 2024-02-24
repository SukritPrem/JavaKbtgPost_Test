package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.exception.ServerInternalErrorException;
import com.kbtg.bootcamp.posttest.exception.Status200Exception;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.userOperation.UserOperation;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicketRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private  final UserRepository userRepository;

    private final LotteryRepository lotteryRepository;

    private final UserTicketRepository userTicketRepository;

    private UserTicketStoreService userTicketStoreService;
    public UserService(UserRepository userRepository,
                       LotteryRepository lotteryRepository,
                       UserTicketRepository userTicketRepository,
                       UserTicketStoreService userTicketStoreService) {
        this.userRepository = userRepository;
        this.lotteryRepository = lotteryRepository;
        this.userTicketRepository = userTicketRepository;
        this.userTicketStoreService =userTicketStoreService;
    }


    public UserOperation checkUserAndLottery(String userId, String ticketId) throws NotFoundException, Status200Exception {
        Optional<User> user = userRepository.findByuserid(userId);
        Optional<Lottery> lotteryOptional = lotteryRepository.findByTicket(ticketId);
        if(user.isEmpty() || lotteryOptional.isEmpty())
            throw new NotFoundException("Error user id or lottery not found");

        Lottery lottery = lotteryOptional.get();
        if(lottery.checkAmounteqaulZero()) {
            throw new Status200Exception("Sorry Lottery Sold out.");
        }
        UserOperation userOperation = new UserOperation();
        userOperation.setLottery(lottery);
        userOperation.setUser(user.get());
        return userOperation;
    }


    @Transactional
    public Integer userBuyTicket(String userId, String ticketId) throws NotFoundException {
        try {
            UserOperation userOperation = checkUserAndLottery(userId, ticketId);
            userOperation = userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperation);
            if (userOperation.getAction().equals("BUY")) {
                return saveUserActionReturnUserTicket(
                        userOperation.getAction(),
                        userOperation.getUserTicketStore().getAmount(),
                        userOperation.getUser(),
                        userOperation.getLottery()
                ).getId();
            } else if (userOperation.getAction().equals("BUY AND UPDATE")) {
                saveUserActionReturnUserTicket(
                        "BUY",
                        userOperation.getLottery().getAmount(),
                        userOperation.getUser(),
                        userOperation.getLottery()
                );
                return saveUserActionReturnUserTicket(
                        "UPDATE",
                        userOperation.getUserTicketStore().getAmount(),
                        userOperation.getUser(),
                        userOperation.getLottery()
                ).getId();
            } else
                throw new ServerInternalErrorException("Error User Buy Ticket");
        } catch (ServerInternalErrorException | Status200Exception e){
            throw new ServerInternalErrorException("Error User Buy Ticket");
        }
    }

    public  ReturnResultAllToUser  allTotalTicket(String userId) throws NotFoundException {
        return userTicketStoreService.SumTicketAndCostAndAmount(userId);
    }

    public UserTicket deleteTicket(String userId,String ticket) throws NotFoundException {

        Optional<User> user = userRepository.findByuserid(userId);
        if(user.isEmpty())
            throw new NotFoundException("Ticket not found or User not found");
        UserTicketStore userTicketStore = userTicketStoreService.deleteTicketInUserTicketStore(userId,ticket);

        return  userTicketRepository.save(
                new UserTicket(
                    userTicketStore.getUserid(),
                    "USER",
                    "DELETE",
                    userTicketStore.getTicket(),
                    userTicketStore.getAmount(),
                    userTicketStore.getPrice()
                )
        );
    }

    // Define a method to save user action

    public UserTicket saveUserActionReturnUserTicket(String actionType, String totalAmount, User user,Lottery lottery)
    {
        return (userTicketRepository.save(
                new UserTicket(
                        user.getUserId(),
                        user.getRoles(),
                        actionType,
                        lottery.getTicket(),
                        totalAmount,
                        lottery.getPrice())
            )
        );
    }

}

// record ReturnAllResultUserTicket(List<String> ticket, Integer price , Integer amount)
//        return new ReturnAllResultUserTicket(
//                listTicketStore
//                        .stream()
//                        .map(UserTicketStore::getTicket)
//                        .toList(),
//                listTicketStore
//                        .stream()
//                        .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount()) *
//                                Integer.parseInt(userLottery.getPrice()))
//                        .sum(),
//                listTicketStore
//                        .stream()
//                        .mapToInt(userTicketStore -> Integer.parseInt(userTicketStore.getAmount()))
//                        .sum()
//        );

//        List<UserTicketStore> listTicketStore = userTicketStoreRepository.findByuserid(userId);
//        if(listTicketStore.isEmpty())
//            throw new NotFoundException("User dosen't have Ticket");
//        List<String> resultAllTicket = listTicketStore
//                .stream()
//                .map(UserTicketStore::getTicket)
//                .toList();
//        Integer reultAllPrice = listTicketStore
//                .stream()
//                .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount()) *
//                        Integer.parseInt(userLottery.getPrice()))
//                .sum();
//        Integer resultAllAmount =   listTicketStore
//                .stream()
//                .mapToInt(userTicketStore -> Integer.parseInt(userTicketStore.getAmount()))
//                .sum();
//        return new ReturnResultAllToUser(resultAllTicket,reultAllPrice,resultAllAmount);