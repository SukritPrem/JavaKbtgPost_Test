package com.kbtg.bootcamp.posttest.usetest;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.UserService;
import com.kbtg.bootcamp.posttest.user.userOperationService.UserOperationsService;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private LotteryRepository lotteryRepository;

    @Mock
    private UserTicketStoreRepository userTicketStoreRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCheckUserAndLottery()
    {
        // Mock user and lottery data
        User user = new User(1,"1234567890","USER","123454321234"); // Assuming admin user is present
        Lottery lottery = new Lottery("123","123456","234");

        when(userRepository.findByuserid("1234567890")).thenReturn(Optional.of(user));
        when(lotteryRepository.findByTicket("123")).thenReturn(Optional.of(lottery));

        UserOperationsService userOperationsService = userService.CheckUserAndLottery("1234567890", "123");

        assertNotNull(userOperationsService);
        assertEquals(user, userOperationsService.getUser());
        assertEquals(lottery, userOperationsService.getLottery());

        verify(userRepository).findByuserid("1234567890");
        verify(lotteryRepository).findByTicket("123");
    }
}
