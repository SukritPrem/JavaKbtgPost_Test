package com.kbtg.bootcamp.posttest.usertest;

import com.kbtg.bootcamp.posttest.user.ReturnResultAllToUser;
import com.kbtg.bootcamp.posttest.user.UserController;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    MockMvc mockMvc;


    @Mock
    private UserRepository userRepository;

@Mock
private UserService userService;
    @BeforeEach
    void setUp() {
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .alwaysDo(print())
                .build();
    }

//    @Test
//    @DisplayName("when perform on GET: /users/me should return Hello, Wallet!")
//    void walletMessage() throws Exception {
//        mockMvc.perform(get("/users/me"))
//                .andExpect(jsonPath("$.message", is("Hello, Wallet!")))
//                .andExpect(status().isOk());
//    }
    @Test
    @DisplayName("when perform on GET: /users/{userId}/lotteries should return " +
            "{" +
            "         ticket : [000001]" +
                    " cost   : 199"+
            "         count  : 1" +
            "}")
    void TestUserGetAllLotteryExpectReturnStatusOK() throws Exception {
        String userId = "0123456789";
        List<String> resultAllticket = new ArrayList<>();
        resultAllticket.add("000001");
        Integer price = 199;
        Integer amount = 1;
        ReturnResultAllToUser result = new ReturnResultAllToUser(resultAllticket,price,amount);
        when(userService.allTotalTicket(userId)).thenReturn(result);

        mockMvc.perform(get("/users/{userId}/lotteries", userId))
                .andExpect(jsonPath("$.tickets", hasSize(1)))
                .andExpect(jsonPath("$.tickets", contains("000001")))
                .andExpect(jsonPath("$.cost",is(199)))
                .andExpect(jsonPath("$.count",is(1)))
                .andExpect(status().isOk());

        verify(userService).allTotalTicket(userId);
    }

    void TestUserGetAllLotteryExpectReturnStatusBad() throws Exception {
        String userId = "0123456789";
        List<String> resultAllticket = new ArrayList<>();
        resultAllticket.add("000001");
        Integer price = 199;
        Integer amount = 1;
        ReturnResultAllToUser result = new ReturnResultAllToUser(resultAllticket,price,amount);
        when(userService.allTotalTicket(userId)).thenReturn(result);

        mockMvc.perform(get("/users/{userId}/lotteries", userId))
                .andExpect(jsonPath("$.tickets", hasSize(1)))
                .andExpect(jsonPath("$.tickets", contains("000001")))
                .andExpect(jsonPath("$.cost",is(199)))
                .andExpect(jsonPath("$.count",is(1)))
                .andExpect(status().isBadRequest());

        verify(userService).allTotalTicket(userId);
    }
//    @Test
//    void savedUserHasRegistrationDate() {
//        User user = new User();
//        user.setRoles("USER");
//        user.setEncoderpassword("09876543");
//        user.setUserId("134567543");
//
//        userRepository.save(user);
//        // Retrieve the saved user
//        Optional<User> savedUserOptional = userRepository.findById(user.getId().longValue());
//
//        // Assert that the saved user exists
//        assertTrue(savedUserOptional.isPresent(), "User should be saved");
//
//        // Get the saved user
//        User savedUser = savedUserOptional.get();
//
//        // Assert that the saved user has a registration date
//
//    }
//record  ReturnAllResultUserTicket(List<String> ticket, Integer price, Integer amount) {
//
//
//}

//    @Test
//    @DisplayName("Test path /user/{userId}/lotteries/{ticketId} when user/00/lotteries/00")
//    public void testAddShouldReturn200Request() throws Exception {
//
//
//
//        Mockito.when(patientRecordRepository.findAll()).thenReturn(records);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/users/0/lotteries/Hello")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }


}
