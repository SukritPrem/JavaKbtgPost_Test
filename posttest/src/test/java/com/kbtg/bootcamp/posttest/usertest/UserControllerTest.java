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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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


    @Test
    @DisplayName("when perform on GET: /users/{userId}/lotteries should return All result")
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
                .andExpect(jsonPath("$.tickets[0]", is("000001")))
                .andExpect(jsonPath("$.cost",is(199)))
                .andExpect(jsonPath("$.count",is(1)))
                .andExpect(status().isOk());

        verify(userService).allTotalTicket(userId);
    }

    @Test
    @DisplayName("Test number character < 10 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathNumberCaseOne() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "123456789"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test number character > 10 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathNumberCaseTwo() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "12345678901"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test String character == 10 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathStringCaseOne() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "abcdefghij"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test number character == 12345678.9 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathNumberCaseThree() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "12345678.9"))
                .andExpect(status().isBadRequest());
    }

    //When GET user pattern regex for filter Path Variable in Error Path Variable another Controller.
    //It's seem logic that I think another controller don't need to Test.
    //Need check Return value.It's return follow requirement.

//    @PostMapping("/{userId}/lotteries/{ticketId}")
//    public Map<String, String> UserBuyTicket(@PathVariable @Pattern(regexp = "\\d{10}") String userId,
//                                             @PathVariable @Pattern(regexp = "\\d{6}") String ticketId) throws NotFoundException {
//        return Map.of(
//                "id", Integer.toString(userService.userBuyTicket(userId, ticketId))
//        );
//    }
}
