package com.kbtg.bootcamp.posttest.user.user_ticket_store;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.exception.ServerInternalErrorException;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.ReturnResultAllToUser;
import com.kbtg.bootcamp.posttest.user.userOperation.UserOperation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTicketStoreService {


    private UserTicketStoreRepository userTicketStoreRepository;

    private LotteryRepository lotteryRepository;


    public UserTicketStoreService(UserTicketStoreRepository userTicketStoreRepository, LotteryRepository lotteryRepository) {
        this.userTicketStoreRepository = userTicketStoreRepository;
        this.lotteryRepository = lotteryRepository;
    }

    @Transactional
    public UserOperation updateUserTicketStoreAndLottery(UserOperation userOperation) throws ServerInternalErrorException {
        try {
                Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByUseridAndTicket(
                    userOperation.getUser().getUserId(),
                    userOperation.getLottery().getTicket()
            );
            if (userTicketStoreOptional.isEmpty()) {
                //insert ticket to user_ticket_store;
                UserTicketStore userTicketStore = new UserTicketStore(
                        userOperation.getUser().getUserId(),
                        userOperation.getLottery().getTicket(),
                        userOperation.getLottery().getAmount(),
                        userOperation.getLottery().getPrice()
                );

                userOperation.setUserTicketStore(userTicketStore);

                return  saveUserTicketStoreAndUpdateLottery(userOperation, "BUY");
            }
            else {
                //user Operation get value userTicketStore
                userOperation.setUserTicketStore(userTicketStoreOptional.get());

                //Re value Amount In user ticketStore when use buy new one
                userOperation.setAmountAndPriceInUseTicketStore();

                return  saveUserTicketStoreAndUpdateLottery(userOperation, "BUY AND UPDATE");
            }
        } catch (ServerInternalErrorException exception) {
            throw new ServerInternalErrorException("Error From UserTicketStoreService Layer When Update.");
        }
    }

    public ReturnResultAllToUser sumTicketAndCostAndAmount(String userid) throws NotFoundException {
        List<UserTicketStore> user = userTicketStoreRepository.findByuserid(userid);
        if (!user.isEmpty()) {
            return new ReturnResultAllToUser(
                    listAllStringTicket(userid),
                    sumAllCost(userid),
                    sumAllAmount(userid)
            );
        } else
            throw new NotFoundException("UserTicketStore Not Found " + userid);
    }


    public UserTicketStore deleteTicketInUserTicketStore(String userId, String ticket) throws NotFoundException {
        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByUseridAndTicket(userId, ticket);
        if (userTicketStoreOptional.isPresent()) {
            userTicketStoreRepository.deleteTicketByuserId(ticket, userId);
            return userTicketStoreOptional.get();
        }
        throw new NotFoundException("UserTicketStore Not Found Lottery :" + ticket);
    }

    public void checkIfPriceLotteryChangeUpdate(String ticket, String price) {
        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByticket(ticket);
        if (userTicketStoreOptional.isPresent()) {
            if (!userTicketStoreOptional.get().getPrice().equals(price))
                userTicketStoreRepository.updatePriceByTicket(price, ticket);
        }
    }

    private List<String> listAllStringTicket(String userid) {
        return userTicketStoreRepository.findDistinctTicketByUserId(userid);
    }

    private Integer sumAllCost(String userid) {
        return userTicketStoreRepository.findUserTicketStoreByUserId(userid)
                .stream()
                .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount()) *
                        Integer.parseInt(userLottery.getPrice()))
                .sum();
    }

    private Integer sumAllAmount(String userid) {
        return userTicketStoreRepository.findUserTicketStoreByUserId(userid)
                .stream()
                .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount()))
                .sum();
    }

    private UserOperation saveUserTicketStoreAndUpdateLottery(UserOperation userOperation,String action)
    {
        //Update database zero because assume User buy all lottery;
        lotteryRepository.updateAmountZeroByticket(userOperation.getLottery().getTicket());
        userOperation.setAction(action);
        if(action.equals("BUY")) {
            userTicketStoreRepository.save(userOperation.getUserTicketStore());
            return userOperation;
        }
        //Update userTicketStore
        userTicketStoreRepository.updateAmountByuserIdAndTicket(
                userOperation.getUserTicketStore().getAmount(),
                userOperation.getUser().getUserId(),
                userOperation.getUserTicketStore().getTicket());

        return userOperation;
    }
}

