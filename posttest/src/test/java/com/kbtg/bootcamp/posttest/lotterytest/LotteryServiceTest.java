package com.kbtg.bootcamp.posttest.lotterytest;

import com.kbtg.bootcamp.posttest.lottery.Lottery;
import com.kbtg.bootcamp.posttest.lottery.LotteryRepository;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.UserService;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LotteryServiceTest {

//    MockMvc mockMvc;
    @Mock
    private LotteryRepository lotteryRepository;


    @Mock
    private UserTicket userTicket;
    @InjectMocks
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
//    @Test
//    public void testGetAllLottery() {
//
//        LotteryService lotteryService1 = new LotteryService(lotteryRepository,userService,userRepository);
//        // Prepare test data
//        List<Lottery> mockLotteryList = new ArrayList<>();
//        mockLotteryList.add(new Lottery("1", "ticket1", "100"));
//        mockLotteryList.add(new Lottery("2", "ticket2", "0")); // Amount is 0
//        mockLotteryList.add(new Lottery("3", "ticket3", "0")); // Amount is negative
//        when(lotteryRepository.findAll()).thenReturn(mockLotteryList);
//
//        // Call the method under test
//        List<String> result = lotteryService1.getAll_lottery();
//
//        // Verify the result
//        List<String> expectedTickets = Arrays.asList("ticket1");
//        System.out.println("Result:");
//        for (String ticket : result) {
//            System.out.println(ticket);
//        }
//        assertEquals(expectedTickets, result);
//    }

    @Test
    public void testCreateNewLotteryByAdmin() {
        LotteryRequest lotteryRequest = new LotteryRequest("123456", 100, 10);
        User adminUser = new User(1,"1234567890","ADMIN","123454321234"); // Assuming admin user is present
        Lottery lottery = new Lottery("123","123456","234");
        Optional<User> adminUserOptional = Optional.of(adminUser);

        UserTicket userTicket1 = new UserTicket("1234567890",
                "ADMIN","ADD","123456","234","123");

        // Stub the behavior of dependencies
        when(userRepository.findByroles("ADMIN")).thenReturn(adminUserOptional);
        when(lotteryRepository.findByTicket(lotteryRequest.getTicket())).thenReturn(Optional.empty()); // Assuming no existing lottery
        when(userService.saveUserActionReturnUserTicket(anyString(), anyInt(), any(User.class), any(Lottery.class))).thenReturn(userTicket1);

        // Call the method under test
        UserTicket userTicket = lotteryService.createNewLotteryByAdmin(lotteryRequest);

        // Verify interactions and method executions
        assertNotNull(userTicket); // Ensure that userTicket is not null
        assertEquals(userTicket1, userTicket); // Ensure that the returned userTicket matches the expected userTicket
        verify(lotteryRepository).save(any(Lottery.class)); // Verify that lotteryRepository.save() was called with any Lottery object
        verify(userService).saveUserActionReturnUserTicket(eq("ADD"), eq(10), eq(adminUser), any(Lottery.class)); // Verify that userService.saveUserActionReturnUserTicket() was called with specific arguments
        verify(userRepository).findByroles("ADMIN"); // Verify that userRepository.findByroles() was called
        verify(lotteryRepository).findByTicket("123456"); // Verify that lotteryRepository.findByTicket() was called
    }


}

//class lotteryNotCallSaveUserService extends LotteryService
//{
//    @Mock
//    UserTicket userTicket;
//
//    public lotteryNotCallSaveUserService(LotteryRepository lotteryRepository, UserService userService, UserRepository userRepository) {
//        super(lotteryRepository, userService, userRepository);
//    }
//
//    @Override
//    public UserTicket createNewLotteryByAdmin(LotteryRequest lotteryRequest){
//        return new UserTicket("1234567890",
//                "ADMIN","ADD","123456","234","123");
//    }
//}
