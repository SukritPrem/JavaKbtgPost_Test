package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicketRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
            //need insert ticket to user_ticket and user_lottery;
            UserTicketStore userSaveTicket = new UserTicketStore();
            userSaveTicket.setAmount(lottery.getAmount());
            userSaveTicket.setTicket(lottery.getTicket());
            userSaveTicket.setUserid(user.get().getUserId());
            userSaveTicket.setPrice(lottery.getPrice());
            userTicketStoreRepository.save(userSaveTicket);
            //  need update database zero because assume User by all lottery;
            lotteryRepository.updateAmountZeroByticket(lottery.getTicket());

            Map<String, String> data = new HashMap<>();
            //need get by id userticket because userticket keep transaction
            data.put("id", Integer.toString(userSaveTicket.getId()));
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        else
        {
            UserTicketStore userLottery = userTicketStoreOptional.get();
            //  need update database zero because assume User by all lottery;
            lotteryRepository.updateAmountZeroByticket(lottery.getTicket());
            int totalAmount = Integer.parseInt(userLottery.getAmount()) +
                    Integer.parseInt(lottery.getAmount());
            //need add amount
            userTicketStoreRepository.updateAmountById(Integer.toString(totalAmount),
                    userLottery.getUserid());
            return ResponseEntity.ok("Update amount in User_lottery");
        }
    }

    public ResponseEntity<?> allTotalTicket(String userId)
    {
//        Optional<UserLottery> userLotteryOptional = userLotteryRepository.findByuserid(userId);
        List<UserTicketStore> listUserLottery = userTicketStoreRepository.findByuserid(userId);
        if(listUserLottery.isEmpty())
            return ResponseEntity.ok("User dosen't have Ticket");
        List<String> listTicket = new ArrayList<>();
        int totalPrice = 0;
        int totalAmount = 0;
        int AmountForCountPrice = 0;
        for (UserTicketStore obj : listUserLottery) {
            listTicket.add(obj.getTicket());
            AmountForCountPrice = Integer.parseInt(obj.getAmount());
            for (int j = AmountForCountPrice; j > 0; j--) {
                totalPrice += Integer.parseInt(obj.getPrice());
            }
            totalAmount += AmountForCountPrice;
        }

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("tickets", listTicket);
        jsonObject.put("count", totalAmount);
        jsonObject.put("cost", totalPrice);

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