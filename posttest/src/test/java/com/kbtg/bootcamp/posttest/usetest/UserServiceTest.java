package com.kbtg.bootcamp.posttest.usetest;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.UserService;
import com.kbtg.bootcamp.posttest.user.userOperationService.UserOperationsService;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicketRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private LotteryRepository lotteryRepository;

    @Mock
    private UserTicketRepository userTicketRepository;
    @Mock
    private UserTicketStoreRepository userTicketStoreRepository;

    @Mock
    private UserTicketStoreService userTicketStoreService;



    @InjectMocks
    private UserService userService;

    @Test
    void testCheckUserAndLottery() throws NotFoundException {
        // Mock user and lottery data
        User user = new User(1,"1234567890","USER","123454321234"); // Assuming admin user is present
        Lottery lottery = new Lottery("123","123456","234");

        when(userRepository.findByuserid("1234567890")).thenReturn(Optional.of(user));
        when(lotteryRepository.findByTicket("123")).thenReturn(Optional.of(lottery));

        UserOperationsService userOperationsService = userService.checkUserAndLottery("1234567890", "123");

        assertNotNull(userOperationsService);
        assertEquals(user, userOperationsService.getUser());
        assertEquals(lottery, userOperationsService.getLottery());

        verify(userRepository).findByuserid("1234567890");
        verify(lotteryRepository).findByTicket("123");
    }

    @Test
    public void testUserBuyTicketWhenActionIsBuy() throws NotFoundException {
        // Arrange
        SpyUserServiceTest spyUserServiceTest = new SpyUserServiceTest(userRepository,lotteryRepository,userTicketRepository,userTicketStoreService);

        UserOperationsService userOperationsService = spyUserServiceTest.checkUserAndLottery("dummy","dummy");

        UserTicket userTicket = new UserTicket(
                userOperationsService.getUser().getUserId(),
                userOperationsService.getUser().getRoles(),
                "BUY",
                userOperationsService.getLottery().getTicket(),
                userOperationsService.getLottery().getAmount(),
                userOperationsService.getLottery().getPrice());
        userTicket.setId(1);

        doReturn(userOperationsService).when(userTicketStoreService).updateUserTicketAndLotteryAndReturnUserId(any());
        doReturn(userTicket).when(userTicketRepository).save(any());

        Integer result = spyUserServiceTest.userBuyTicket(
                userOperationsService.getUser().getUserId(),
                userOperationsService.getLottery().getTicket()
        );

        assertEquals(userTicket.getId(), result);

        verify(userTicketStoreService).updateUserTicketAndLotteryAndReturnUserId(any());
        verify(userTicketRepository).save(any());
    }


}

class SpyUserServiceTest extends UserService
{
    public SpyUserServiceTest(UserRepository userRepository,
                              LotteryRepository lotteryRepository,
                              UserTicketRepository userTicketRepository,
                              UserTicketStoreService userTicketStoreService) {
        super(userRepository, lotteryRepository, userTicketRepository, userTicketStoreService);
    }

    @Override
    public UserOperationsService checkUserAndLottery(String userId,String ticketId) throws NotFoundException
    {
        User user = new User(1,"1234567890","USER","123454321234"); // Assuming admin user is present
        Lottery lottery = new Lottery("123","123456","234");
        UserTicketStore userTicketStore= new UserTicketStore(user.getUserId(),
                lottery.getTicket(),
                lottery.getPrice(),
                lottery.getAmount());
        UserOperationsService userOperationsService = new UserOperationsService();
        userOperationsService.setLottery(lottery);
        userOperationsService.setUser(user);
        userOperationsService.setUserTicketStore(userTicketStore);
        userOperationsService.setAction("BUY");
        return userOperationsService;
    };

    @Override
    public Integer userBuyTicket(String userid, String ticket) throws NotFoundException {
        return super.userBuyTicket(userid,ticket);
    };


}

