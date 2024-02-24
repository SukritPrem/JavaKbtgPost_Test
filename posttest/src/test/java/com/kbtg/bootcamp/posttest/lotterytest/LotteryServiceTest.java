package com.kbtg.bootcamp.posttest.lotterytest;

import com.kbtg.bootcamp.posttest.exception.ServerInternalErrorException;
import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LotteryServiceTest {

//    MockMvc mockMvc;
    @Mock
    private LotteryRepository lotteryRepository;


    @Mock
    private  LotteryService lotteryService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
//    @BeforeEach
//    void setUp() {
//        LotteryService lotteryService1 = new LotteryService(lotteryRepository,userService);
////        mockMvc = MockMvcBuilders.standaloneSetup(lotteryService1)
////                .alwaysDo(print())
////                .build();
//    }
    @Test
    public void testGetAllLottery() {

        LotteryService lotteryService1 = new LotteryService(lotteryRepository,userService,userRepository);
        // Prepare test data
        List<Lottery> mockLotteryList = new ArrayList<>();
        mockLotteryList.add(new Lottery("1", "ticket1", "100"));
        mockLotteryList.add(new Lottery("2", "ticket2", "0")); // Amount is 0
        mockLotteryList.add(new Lottery("3", "ticket3", "0")); // Amount is negative
        when(lotteryRepository.findAll()).thenReturn(mockLotteryList);

        // Call the method under test
        List<String> result = lotteryService1.getAll_lottery();

        // Verify the result
        List<String> expectedTickets = Arrays.asList("ticket1");
        System.out.println("Result:");
        for (String ticket : result) {
            System.out.println(ticket);
        }
        assertEquals(expectedTickets, result);
    }

    @Test
    public void testCreateNewLotteryByAdmin() throws ServerInternalErrorException {
        // Prepare test data
        LotteryRequest lotteryRequest = new LotteryRequest("ticket1", 100, 10);
        User adminUser = new User(); // Assuming admin user is present
        adminUser.setRoles("ADMIN");
        Optional<User> adminUserOptional = Optional.of(adminUser);
        when(userRepository.findByroles("ADMIN")).thenReturn(adminUserOptional);

        Optional<Lottery> existingLotteryOptional = Optional.empty();
        when(lotteryRepository.findByTicket(lotteryRequest.getTicket())).thenReturn(existingLotteryOptional);

        // Call the method under test
        lotteryService.createNewLotteryByAdmin(lotteryRequest);

        // Verify interactions
        verify(lotteryRepository).save(any(Lottery.class));
        verify(userService).saveUserActionReturnUserTicket(eq("ADD"), eq(10), eq(adminUser), any(Lottery.class));
    }
}
