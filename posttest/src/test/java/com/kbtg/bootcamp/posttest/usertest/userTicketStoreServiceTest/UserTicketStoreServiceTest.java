package com.kbtg.bootcamp.posttest.usertest.userTicketStoreServiceTest;

import com.kbtg.bootcamp.posttest.exception.NotFoundException;
import com.kbtg.bootcamp.posttest.exception.ServerInternalErrorException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.user.ReturnResultAllToUser;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.userOperation.UserOperation;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStore;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreRepository;
import com.kbtg.bootcamp.posttest.user.user_ticket_store.UserTicketStoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTicketStoreServiceTest {

    @Mock
    UserOperation userOperation;

    @Mock
    UserTicketStoreRepository userTicketStoreRepository;

    @Mock
    LotteryRepository lotteryRepository;

    @InjectMocks
    UserTicketStoreService userTicketStoreService;



    @Test
    @DisplayName("Expect action BUY When user Buy Lottery")
    public void testUpdateUserTicketAndLotteryAndReturnUserId_whenUserTicketStoreNotFound()
    {
        // Mocking userOperationsService methods
        UserOperation userOperation =new UserOperation();
        User user = new User(1,"1234567890","USER","123454321234"); // Assuming admin user is present
        Lottery lottery = new Lottery("123","123456","234");
        userOperation.setUser(user);
        userOperation.setLottery(lottery);

        // Mocking repository behavior
        when(userTicketStoreRepository.findByUseridAndTicket(any(), any())).thenReturn(Optional.empty());

        // Call the method under test
        UserOperation result = userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperation);

        assertEquals(userOperation, result);
        assertEquals("BUY", result.getAction());

        verify(userTicketStoreRepository).save(any(UserTicketStore.class));
        verify(lotteryRepository).updateAmountZeroByticket(any());
    }

    @Test
    @DisplayName("Expect action BUY And UPDATE When user Buy Lottery " +
                "and user have Lottery in user_ticet_store")
    public void testWhenUserBuyCaseUpdate()
    {
        // Mocking userOperationsService methods
        UserOperation userOperation =new UserOperation();
        User user = new User(1,"1234567890","USER","123454321234"); // Assuming admin user is present
        Lottery lottery = new Lottery("123","123456","234");
        userOperation.setUser(user);
        userOperation.setLottery(lottery);
        UserTicketStore userTicketStore= new UserTicketStore(user.getUserId(),
                lottery.getTicket(),
                lottery.getPrice(),
                lottery.getAmount());

        // Mocking repository behavior
        when(userTicketStoreRepository.findByUseridAndTicket(any(), any())).thenReturn(Optional.of(userTicketStore));

        // Call the method under test
        UserOperation result = userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperation);

        assertEquals(userOperation, result);
        assertEquals("BUY AND UPDATE", result.getAction());

        verify(lotteryRepository).updateAmountZeroByticket(any());
    }

    @Test
    @DisplayName("Make Sure UserOperation get UserticketStore When BUY AND UPDATE")
    public void MakeSureUserOperationGetUserTicketStoreAlready()
    {
        // Mocking userOperationsService methods
        UserOperation userOperation =new UserOperation();
        User user = new User(1,"1234567890","USER","123454321234"); // Assuming admin user is present
        Lottery lottery = new Lottery("123","123456","234");
        userOperation.setUser(user);
        userOperation.setLottery(lottery);
        UserTicketStore userTicketStore= new UserTicketStore(user.getUserId(),
                lottery.getTicket(),
                lottery.getPrice(),
                lottery.getAmount());

        // Mocking repository behavior
        when(userTicketStoreRepository.findByUseridAndTicket(any(), any())).thenReturn(Optional.of(userTicketStore));

        // Call the method under test
        Integer expect = Integer.parseInt(lottery.getAmount()) + Integer.parseInt(userTicketStore.getAmount());
        UserOperation result = userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperation);

        assertEquals(Integer.toString(expect), result.getUserTicketStore().getAmount());
        assertEquals("BUY AND UPDATE", result.getAction());

        verify(lotteryRepository).updateAmountZeroByticket(any());
    }

    @Test
    public void testUpdateUserTicketAndLotteryAndReturnUserId_throwsServerInternalErrorException() {
        // Mocking userOperationsService methods
        UserOperation userOperation =new UserOperation();
        User user = new User(1,"1234567890","USER","123454321234"); // Assuming admin user is present
        Lottery lottery = new Lottery("123","123456","234");
        userOperation.setUser(user);
        userOperation.setLottery(lottery);

        // Mocking repository behavior to throw ServerInternalErrorException
        when(userTicketStoreRepository.findByUseridAndTicket(any(), any()))
                .thenThrow(new ServerInternalErrorException("Some error message"));

        // Assertions
        assertThrows(ServerInternalErrorException.class, () -> {
            userTicketStoreService.updateUserTicketAndLotteryAndReturnUserId(userOperation);
        });
    }

    @Test
    public void testSumTicketAndCostAndAmount_whenUserFound() throws NotFoundException {
        // Mocking userTicketStoreRepository behavior
        UserTicketStore userTicketStore1 = new UserTicketStore();
        userTicketStore1.setAmount("69");
        userTicketStore1.setTicket("123456");
        userTicketStore1.setUserid("1234567890");
        userTicketStore1.setPrice("400");
        UserTicketStore userTicketStore = new UserTicketStore();
        userTicketStore.setAmount("69");
        userTicketStore.setTicket("123457");
        userTicketStore.setUserid("1234567890");
        userTicketStore.setPrice("400");
        List<UserTicketStore> listUser = new ArrayList<>();
        listUser.add(userTicketStore);
        listUser.add(userTicketStore1);

        List<String> amount = listUser.stream().map(user -> user.getAmount()).collect(Collectors.toList());

        Integer cost =  listUser.stream().mapToInt(userLottery -> Integer.parseInt(userLottery.getPrice())
                        * Integer.parseInt(userLottery.getAmount())).sum();

        Integer amountInt =  listUser.stream()
                .mapToInt(userLottery -> Integer.parseInt(userLottery.getAmount())).sum();

        List<String> tickets = listUser.stream().map(user -> user.getTicket()).collect(Collectors.toList());

        ReturnResultAllToUser expect = new ReturnResultAllToUser(tickets,cost,amountInt);


        when(userTicketStoreRepository.findByuserid(anyString())).thenReturn(listUser);
        when(userTicketStoreRepository.findDistinctTicketByUserId(anyString())).thenReturn(tickets);
        when(userTicketStoreRepository.sumPriceByUserId(anyString())).thenReturn(listUser);
        when(userTicketStoreRepository.sumAmountByUserId(anyString())).thenReturn(amount);



        // Call the method under test
        ReturnResultAllToUser result = userTicketStoreService.SumTicketAndCostAndAmount("123456");

        assertEquals(expect.getTickets(),result.getTickets());
        assertEquals(expect.getCost(),result.getCost());
        assertEquals(expect.getCount(),result.getCount());

    }
}

