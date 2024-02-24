package com.kbtg.bootcamp.posttest.user.user_ticket_store;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.exception.ServerInternalErrorException;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.ReturnResultAllToUser;
import com.kbtg.bootcamp.posttest.user.userOperationService.UserOperationsService;
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
    public UserOperationsService updateUserTicketAndLotteryAndReturnUserId(UserOperationsService userOperationsService) throws ServerInternalErrorException
    {
        try {
        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByUseridAndTicket(
                userOperationsService.getUser().getUserId(),
                userOperationsService.getLottery().getTicket()
        );
            if (userTicketStoreOptional.isEmpty()) {
                //insert ticket to user_ticket_store;
                UserTicketStore userTicketStore = new UserTicketStore(
                        userOperationsService.getUser().getUserId(),
                        userOperationsService.getLottery().getTicket(),
                        userOperationsService.getLottery().getAmount(),
                        userOperationsService.getLottery().getPrice()
                );

                userTicketStoreRepository.save(userTicketStore);
                userOperationsService.setUserTicketStore(userTicketStore);
                //update database zero because assume User by all lottery;
                lotteryRepository.updateAmountZeroByticket(
                        userOperationsService.getLottery().getTicket()
                );
                userOperationsService.setAction("BUY");
                return userOperationsService;
            } else {
                //Update database zero because assume User buy all lottery;
                userOperationsService.setUserTicketStore(userTicketStoreOptional.get());
                lotteryRepository.updateAmountZeroByticket(userOperationsService.getLottery().getTicket());
                userOperationsService.setAmountInUseTicketStore();
                //Update amount
                userTicketStoreRepository.updateAmountByuserIdAndTicket(
                        userOperationsService.getUserTicketStore().getAmount(),
                        userOperationsService.getUser().getUserId(),
                        userOperationsService.getUserTicketStore().getTicket());
                userOperationsService.setAction("BUY AND UPDATE");
                return userOperationsService;
            }
        }catch (ServerInternalErrorException exception)
        {
            throw new ServerInternalErrorException("Error From UserTicketStoreService Layer When Update.");
        }
    }

    public ReturnResultAllToUser SumTicketAndCostAndAmount(String userid) throws NotFoundException {
        List<UserTicketStore> user = userTicketStoreRepository.findByuserid(userid);
        if(!user.isEmpty()) {
            return new ReturnResultAllToUser(
                    userTicketStoreRepository.findDistinctTicketByUserId(userid),
                    userTicketStoreRepository.sumPriceByUserId(userid)
                            .stream()
                            .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount()) *
                                Integer.parseInt(userLottery.getPrice()))
                            .sum(),
                    userTicketStoreRepository.sumPriceByUserId(userid)
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
        else
            throw new NotFoundException("UserTicketStore Not Found");
    }


}

