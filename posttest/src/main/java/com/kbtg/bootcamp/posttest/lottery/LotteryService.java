package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotteryService {

    private LotteryRepository lotteryRepository;

    public LotteryService(LotteryRepository lotteryRepository) {
        this.lotteryRepository = lotteryRepository;
    }

    public List<String> getAll_lottery() {
        List<Lottery> lotteryList = lotteryRepository.findAll();
        return lotteryList.stream()
                .map(Lottery::getTicket) // Extracting the ticket value
                .collect(Collectors.toList()); // Collecting into a List<String>
    }

    public ResponseEntity<?> createNewLottery(LotteryRequest lotteryRequest) throws NotFoundException {
        Optional<Lottery> lotteryOptional = lotteryRepository.findByTicket(lotteryRequest.getTicket());
        if(lotteryOptional.isEmpty())
        {
            Lottery newLottery = new Lottery();
            newLottery.setTicket(lotteryRequest.getTicket());
            newLottery.setPrice(Integer.toString(lotteryRequest.getPrice()));
            newLottery.setAmount(Integer.toString(lotteryRequest.getAmount()));
            lotteryRepository.save(newLottery);
            return new ResponseEntity<>(newLottery, HttpStatus.OK);
        }
        else
        {
            Lottery lottery = lotteryOptional.get();
            int total = Integer.parseInt(lottery.getAmount()) + lotteryRequest.getAmount();
            lotteryRepository.updateAmountByticket(Integer.toString(total),lotteryRequest.getTicket());
            //Update price;
            lotteryRepository.updatePriceByticket(Integer.toString(lotteryRequest.getPrice()),lotteryRequest.getTicket());
            return ResponseEntity.ok("Update Amount and Price");
        }
    }

}