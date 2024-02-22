package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicketRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public ResponseEntity<?>  UserBuyTicket(String userId,String ticketId) throws NotFoundException {
        Optional<User> user = userRepository.findByuserid(userId);
        Optional<Lottery> lotteryOptional = lotteryRepository.findByticket(ticketId);

        if(user.isEmpty() || lotteryOptional.isEmpty())
            throw new NotFoundException("Error user id or lottery not found");

        Lottery lottery = lotteryOptional.get();
        if(lottery.checkAmounteqaulZero())
            ResponseEntity.ok("Sorry Lottery Sold out.");

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
            UserTicket userTicket = userTicketRepository.save(new UserTicket(
                    user.get().getUserId(),
                    "BUY",
                    lottery.getTicket(),
                    lottery.getAmount(),
                    lottery.getPrice()));

            //Set output {"id" : "userTicket::id"}
            Map<String, String> data = new HashMap<>();
            data.put("id", Integer.toString(userTicket.getId()));
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        else
        {
            UserTicketStore userLottery = userTicketStoreOptional.get();

            //Update database zero because assume User buy all lottery;
            lotteryRepository.updateAmountZeroByticket(lottery.getTicket());
            int totalAmount = Integer.parseInt(userLottery.getAmount()) +
                    Integer.parseInt(lottery.getAmount());

            //Update amount
            userTicketStoreRepository.updateAmountById(Integer.toString(totalAmount),
                    userLottery.getUserid());

            return ResponseEntity.ok("Update amount in User_lottery");
        }
    }

    public ResponseEntity<?> allTotalTicket(String userId)
    {
        List<UserTicketStore> listTicketStore = userTicketStoreRepository.findByuserid(userId);
        if(listTicketStore.isEmpty())
            return ResponseEntity.ok("User dosen't have Ticket");

        List<String> listTicket = listTicketStore.stream()
                .map(UserTicketStore::getTicket)
                .collect(Collectors.toList());

        int totalPrice =  listTicketStore .stream()
                .mapToInt(userLottery -> Integer.parseInt(userLottery.getPrice()))
                .sum();;
        int totalAmount = listTicketStore .stream()
                .mapToInt(userTicketStore -> Integer.parseInt(userTicketStore.getAmount()))
                .sum();

//        int AmountForCountPrice = 0;
//        for (UserTicketStore obj : listUserLottery) {
//            listTicket.add(obj.getTicket());
//            AmountForCountPrice = Integer.parseInt(obj.getAmount());
//            for (int j = AmountForCountPrice; j > 0; j--) {
//                totalPrice += Integer.parseInt(obj.getPrice());
//            }
//            totalAmount += AmountForCountPrice;
//        }

        Map<String, Object> jsonObject = Map.of(
                "tickets", listTicket,
                "count", totalAmount,
                "cost", totalPrice
        );
        return new ResponseEntity<>(jsonObject,HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTicket(String userId,String ticket) throws NotFoundException {
        Optional<UserTicketStore> userTicketOptional = userTicketStoreRepository.findByUseridAndTicket(userId, ticket);
        if(userTicketOptional.isEmpty())
            throw new NotFoundException("Ticket not found");
        userTicketStoreRepository.deleteTicketByuserId(ticket,userId);
        Map<String, String> jsonObject = new HashMap<>();
        jsonObject.put("tickets",ticket);
        return ResponseEntity.ok(jsonObject);
    }
}