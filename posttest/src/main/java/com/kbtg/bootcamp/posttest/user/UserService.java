package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.userOperationService.UserOperationsService;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicketRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
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

    public List<User> getUser() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    @Transactional
    public UserOperationsService CheckUserAndLottery(String userId,String ticketId) throws NotFoundException {
        Optional<User> user = userRepository.findByuserid(userId);
        Optional<Lottery> lotteryOptional = lotteryRepository.findByticket(ticketId);
        if(user.isEmpty() || lotteryOptional.isEmpty())
            throw new NotFoundException("Error user id or lottery not found");

        Lottery lottery = lotteryOptional.get();
        if(lottery.checkAmounteqaulZero()) {
            throw new NotFoundException("Sorry Lottery Sold out.");
        }
        UserOperationsService userOperationsService = new UserOperationsService();
        userOperationsService.setLottery(lottery);
        userOperationsService.setUser(user.get());
        return userOperationsService;
    }
    public Integer UserBuyTicket(String userId,String ticketId) throws NotFoundException {
        UserOperationsService userOperationsService = CheckUserAndLottery(userId, ticketId);
        userOperationsService = userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperationsService);
        if (userOperationsService.getAction().equals("BUY"))
        {
            return saveUserActionReturnId(
                    userOperationsService.getAction(),
                    userOperationsService.getUserTicketStore().getAmount(),
                    userOperationsService.getUser(),
                    userOperationsService.getLottery()
            );
        }
        else if (userOperationsService.getAction().equals("BUY AND UPDATE"))
        {
            saveUserAction(
                    "BUY",
                    userOperationsService.getLottery().getAmount(),
                    userOperationsService.getUser(),
                    userOperationsService.getLottery()
            );
            return saveUserActionReturnId(
                    "UPDATE",
                    userOperationsService.getUserTicketStore().getAmount(),
                    userOperationsService.getUser(),
                    userOperationsService.getLottery()
            );
        }
        else
            throw new NotFoundException("Error User Buy Ticket");
    }

    public  ReturnResultAllToUser  allTotalTicket(String userId) throws NotFoundException {
        return userTicketStoreService.SumTicketAndCostAndAmount(userId);
    }
//
    public UserTicket deleteTicket(String userId,String ticket) throws NotFoundException {

        Optional<User> user = userRepository.findByuserid(userId);
        if(user.isEmpty())
            throw new NotFoundException("Ticket not found or User not found");
        UserTicketStore userTicketStore = userTicketStoreService.deleteTicketInUserTicketStore(userId,ticket);

        return  userTicketRepository.save(new UserTicket(
                userTicketStore.getUserid(),
                "USER",
                "DELETE",
                userTicketStore.getTicket(),
                userTicketStore.getAmount(), // Assuming totalAmount is an int
                userTicketStore.getPrice())
        );
    }

    // Define a method to save user action
    public void saveUserAction(String actionType, String totalAmount, User user, Lottery lottery) {
        userTicketRepository.save(new UserTicket(
                user.getUserId(),
                user.getRoles(),
                actionType,
                lottery.getTicket(),
                totalAmount, // Assuming totalAmount is an int
                lottery.getPrice()));
    }

    public UserTicket saveUserActionReturnUserTicket(String actionType, int totalAmount, User user,Lottery lottery) {
        return (userTicketRepository.save(new UserTicket(
                user.getUserId(),
                user.getRoles(),
                actionType,
                lottery.getTicket(),
                Integer.toString(totalAmount), // Assuming totalAmount is an int
                lottery.getPrice())));
    }

    public int saveUserActionReturnId(String actionType, String totalAmount, User user, Lottery lottery) {
        UserTicket userTicket = userTicketRepository.save(new UserTicket(
                user.getUserId(),
                user.getRoles(),
                actionType,
                lottery.getTicket(),
                totalAmount, // Assuming totalAmount is an int
                lottery.getPrice()));
        return userTicket.getId();
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