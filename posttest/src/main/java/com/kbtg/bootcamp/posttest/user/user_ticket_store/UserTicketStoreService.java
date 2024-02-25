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


    public UserTicketStoreService(UserTicketStoreRepository userTicketStoreRepository,LotteryRepository lotteryRepository)
    {
        this.userTicketStoreRepository =userTicketStoreRepository;
        this.lotteryRepository =lotteryRepository;
    }

    @Transactional
    public UserOperation updateUserTicketAndLotteryAndReturnUserId(UserOperation userOperation) throws ServerInternalErrorException
    {
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
                userTicketStoreRepository.save(userTicketStore);

                userOperation.setUserTicketStore(userTicketStore);
                //update database zero because assume User by all lottery;
                lotteryRepository.updateAmountZeroByticket(
                        userOperation.getLottery().getTicket()
                );
                userOperation.setAction("BUY");
                return userOperation;
            } else {
                //user Operation get value userTicketStore
                userOperation.setUserTicketStore(userTicketStoreOptional.get());
                //Update database zero because assume User buy all lottery;
                lotteryRepository.updateAmountZeroByticket(userOperation.getLottery().getTicket());
                //Re value Amount In user ticketStore when use buy new one
                userOperation.setAmountInUseTicketStore();
                userOperation.setPriceInUserTicketStore();
                //Update userTicketStore
                userTicketStoreRepository.updateAmountByuserIdAndTicket(
                        userOperation.getUserTicketStore().getAmount(),
                        userOperation.getUser().getUserId(),
                        userOperation.getUserTicketStore().getTicket());

                userOperation.setAction("BUY AND UPDATE");
                return userOperation;
            }
        }catch (ServerInternalErrorException exception)
        {
            throw new ServerInternalErrorException("Error From UserTicketStoreService Layer When Update.");
        }
    }

    public ReturnResultAllToUser sumTicketAndCostAndAmount(String userid) throws NotFoundException {
        List<UserTicketStore> user = userTicketStoreRepository.findByuserid(userid);
        if(!user.isEmpty()) {
            return new ReturnResultAllToUser(
                    userTicketStoreRepository.findDistinctTicketByUserId(userid),
                    userTicketStoreRepository.findUserTicketStoreByUserId(userid)
                            .stream()
                            .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount()) *
                                Integer.parseInt(userLottery.getPrice()))
                            .sum(),
                    userTicketStoreRepository.findUserTicketStoreByUserId(userid)
                            .stream()
                            .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount()))
                            .sum()
            );
        }
        else
            throw new NotFoundException("Not found User in UserTicketStoreService Layer");
    }


    public UserTicketStore deleteTicketInUserTicketStore(String userId, String ticket) throws NotFoundException
    {
        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByUseridAndTicket(userId, ticket);
        if(userTicketStoreOptional.isPresent())
        {
            userTicketStoreRepository.deleteTicketByuserId(ticket,userId);
            return userTicketStoreOptional.get();
        }
        throw new NotFoundException("UserTicketStore Not Found");
    }

    public void checkIfPriceLotteryChangeUpdate(String ticket,String price)
    {
        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByticket(ticket);
        if(userTicketStoreOptional.isPresent())
        {
            if(!userTicketStoreOptional.get().getPrice().equals(price))
                userTicketStoreRepository.updatePriceByTicket(price,ticket);
        }
    }
}

