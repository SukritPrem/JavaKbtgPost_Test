package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicketRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final LotteryRepository lotteryRepository;

    private final UserTicketRepository userTicketRepository;

    private final UserTicketStoreRepository userTicketStoreRepository;

    public UserService(UserRepository userRepository,
                       LotteryRepository lotteryRepository,
                       UserTicketRepository userTicketRepository,
                       UserTicketStoreRepository userTicketStoreRepository) {
        this.userRepository = userRepository;
        this.lotteryRepository = lotteryRepository;
        this.userTicketRepository = userTicketRepository;
        this.userTicketStoreRepository =userTicketStoreRepository;

    }

    public List<User> getUser() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    @Transactional
    public Integer UserBuyTicket(String userId,String ticketId) throws NotFoundException {
        Optional<User> user = userRepository.findByuserid(userId);
        Optional<Lottery> lotteryOptional = lotteryRepository.findByticket(ticketId);

        if(user.isEmpty() || lotteryOptional.isEmpty())
            throw new NotFoundException("Error user id or lottery not found");

        Lottery lottery = lotteryOptional.get();
        if(lottery.checkAmounteqaulZero()) {
            throw new NotFoundException("Sorry Lottery Sold out.");
        }

        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByUseridAndTicket(user.get().getUserId(),lottery.getTicket());
        if(userTicketStoreOptional.isEmpty())
        {
            //insert ticket to user_ticket_store;
            userTicketStoreRepository.save(new UserTicketStore(
                    user.get().getUserId(),
                    lottery.getTicket(),
                    lottery.getAmount(),
                    lottery.getPrice()));

            //update database zero because assume User by all lottery;
            lotteryRepository.updateAmountZeroByticket(lottery.getTicket());

            //save user_action
            return saveUserActionReturnId("BUY",
                    Integer.parseInt(lottery.getAmount()),
                    user.get(),
                    lottery);
        }
        else
        {
            UserTicketStore userLottery = userTicketStoreOptional.get();

            //Update database zero because assume User buy all lottery;
            lotteryRepository.updateAmountZeroByticket(lottery.getTicket());
            Integer totalAmount = Integer.parseInt(userLottery.getAmount()) +
                    Integer.parseInt(lottery.getAmount());
            System.out.print("\ntotal amount user :" + totalAmount + "\n");
            //Update amount
            userTicketStoreRepository.updateAmountByuserIdAndTicket(Integer.toString(totalAmount),
                    userLottery.getUserid(),
                    lottery.getTicket());

            //save user_action
            saveUserAction("BUY",
                    Integer.parseInt(lottery.getAmount()),
                    user.get(),
                    lottery);
            //save user_action
            return saveUserActionReturnId("UPDATE", Integer.parseInt(lottery.getAmount()), user.get(), lottery);
        }
    }

    public ReturnAllResultUserTicket allTotalTicket(String userId) throws NotFoundException {
        List<UserTicketStore> listTicketStore = userTicketStoreRepository.findByuserid(userId);
        if(listTicketStore.isEmpty())
            throw new NotFoundException("User dosen't have Ticket");
        return new ReturnAllResultUserTicket(
                listTicketStore
                        .stream()
                        .map(UserTicketStore::getTicket)
                        .toList(),
                listTicketStore
                        .stream()
                        .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount()) *
                                Integer.parseInt(userLottery.getPrice()))
                        .sum(),
                listTicketStore
                        .stream()
                        .mapToInt(userTicketStore -> Integer.parseInt(userTicketStore.getAmount()))
                        .sum()
        );
    }

    public UserTicket deleteTicket(String userId,String ticket) throws NotFoundException {
        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByUseridAndTicket(userId, ticket);
        Optional<User> user = userRepository.findByuserid(userId);
        if(userTicketStoreOptional.isEmpty() || user.isEmpty())
            throw new NotFoundException("Ticket not found or User not found");
        userTicketStoreRepository.deleteTicketByuserId(ticket,userId);
        Lottery lottery = new Lottery();
        lottery.setTicket(userTicketStoreOptional.get().getTicket());
        lottery.setPrice(userTicketStoreOptional.get().getPrice());
        return saveUserActionReturnUserTicket(
                "DELETE",
                Integer.parseInt(userTicketStoreOptional.get().getAmount()),
                user.get(),
                lottery
        );
    }

    // Define a method to save user action
    private void saveUserAction(String actionType, int totalAmount, User user,Lottery lottery) {
        userTicketRepository.save(new UserTicket(
                user.getUserId(),
                user.getRoles(),
                actionType,
                lottery.getTicket(),
                Integer.toString(totalAmount), // Assuming totalAmount is an int
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

    private int saveUserActionReturnId(String actionType, int totalAmount, User user,Lottery lottery) {
        UserTicket userTicket = userTicketRepository.save(new UserTicket(
                user.getUserId(),
                user.getRoles(),
                actionType,
                lottery.getTicket(),
                Integer.toString(totalAmount), // Assuming totalAmount is an int
                lottery.getPrice()));
        return userTicket.getId();
    }
}

record ReturnAllResultUserTicket(List<String> ticket, Integer price, Integer amount) {}