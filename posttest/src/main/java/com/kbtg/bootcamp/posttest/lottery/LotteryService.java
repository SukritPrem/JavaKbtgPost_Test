package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotteryService {

    private LotteryRepository lotteryRepository;

    private UserRepository userRepository;

    private UserTicketRepository userTicketRepository;

    public LotteryService(LotteryRepository lotteryRepository,
                          UserTicketRepository userTicketRepository,
                          UserRepository userRepository) {
        this.lotteryRepository = lotteryRepository;
        this.userTicketRepository = userTicketRepository;
        this.userRepository = userRepository;
    }

    public List<String> getAll_lottery() {
        List<Lottery> lotteryList = lotteryRepository.findAll();
        return lotteryList.stream()
                .map(Lottery::getTicket)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> createNewLotteryByAdmin(LotteryRequest lotteryRequest) throws NotFoundException {
        Optional<Lottery> lotteryOptional = lotteryRepository.findByTicket(lotteryRequest.getTicket());

        //assume admin have 1 person
        Optional<User> user = userRepository.findByroles("ADMIN");

        if(lotteryOptional.isEmpty())
        {
            //Create new Lottery
            Lottery newLottery = new Lottery();
            newLottery.setTicket(lotteryRequest.getTicket());
            newLottery.setPrice(Integer.toString(lotteryRequest.getPrice()));
            newLottery.setAmount(Integer.toString(lotteryRequest.getAmount()));
            lotteryRepository.save(newLottery);

            //save user_action
            UserTicket userTicket = userTicketRepository.save(new UserTicket(
                    user.get().getUserId(),
                    user.get().getRoles(),
                    "ADD",
                    newLottery.getTicket(),
                    newLottery.getAmount(),
                    newLottery.getPrice()));
            return new ResponseEntity<>(newLottery, HttpStatus.OK);
        }
        else
        {
            Lottery lottery = lotteryOptional.get();

            int total = Integer.parseInt(lottery.getAmount()) + lotteryRequest.getAmount();

            //Update amount;
            lotteryRepository.updateAmountByticket(Integer.toString(total),lotteryRequest.getTicket());

            //Update price;
            lotteryRepository.updatePriceByticket(Integer.toString(lotteryRequest.getPrice()),lotteryRequest.getTicket());

            return ResponseEntity.ok("Update Amount and Price");
        }
    }

}