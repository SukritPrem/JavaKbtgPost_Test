package com.kbtg.bootcamp.posttest.user.user_ticket_store;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.userOperationService.UserOperationsService;
import org.springframework.stereotype.Service;

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
    public UserOperationsService updateUserTicketAndLotteryAndReturnUserId(UserOperationsService userOperationsService) throws NotFoundException
    {
        try {
        Optional<UserTicketStore> userTicketStoreOptional = userTicketStoreRepository.findByUseridAndTicket(
                userOperationsService.getUser().getUserId(),
                userOperationsService.getLottery().getTicket()
        );
            if (userTicketStoreOptional.isEmpty()) {
                System.out.print("HelloHiService\n");
                //insert ticket to user_ticket_store;
                UserTicketStore userTicketStore = new UserTicketStore(
                        userOperationsService.getUser().getUserId(),
                        userOperationsService.getLottery().getTicket(),
                        userOperationsService.getLottery().getAmount(),
                        userOperationsService.getLottery().getPrice());
                userTicketStoreRepository.save(userTicketStore);
                userOperationsService.setUserTicketStore(userTicketStore);
                //update database zero because assume User by all lottery;
                lotteryRepository.updateAmountZeroByticket(
                        userOperationsService.getLottery().getTicket()
                );
                userOperationsService.setAction("BUY");
                return userOperationsService;
            } else {
//                UserTicketStore userLottery = userTicketStoreOptional.get();
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
        }catch (Exception exception)
        {
            throw new NotFoundException("Error From UserTicketStoreService Layer.");
        }
    }

}
