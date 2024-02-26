package com.kbtg.bootcamp.posttest.usertest;

import com.kbtg.bootcamp.posttest.security.CustomAuthenticationManager;
import com.kbtg.bootcamp.posttest.security.DomainExtractor;
import com.kbtg.bootcamp.posttest.security.JWT.JwtAuthFilter;
import com.kbtg.bootcamp.posttest.security.JWT.JwtService;
import com.kbtg.bootcamp.posttest.user.UserResponse;
import com.kbtg.bootcamp.posttest.user.UserController;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import com.kbtg.bootcamp.posttest.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {


    @MockBean
    UserRepository userRepository;
    @MockBean
    CustomAuthenticationManager customAuthenticationManager;

    @MockBean
    DomainExtractor domainExtractor;

    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @MockBean
    JwtService jwtService;

    @MockBean
    private UserService userService;

    @Autowired
    MockMvc mockMvc;
    @Test
    void testExample() throws Exception {
        String userId = "1234567890";
        List<String> resultAllticket = new ArrayList<>();
        resultAllticket.add("000001");
        Integer price = 199;
        Integer amount = 1;
        UserResponse result = new UserResponse(resultAllticket,price,amount);
        when(userService.allTotalTicket(userId)).thenReturn(result);
        mockMvc.perform(get("/users/{userId}/lotteries", userId))
                .andExpect(jsonPath("$.tickets", hasSize(1)))
                .andExpect(jsonPath("$.tickets[0]", is("000001")))
                .andExpect(jsonPath("$.cost",is(199)))
                .andExpect(jsonPath("$.count",is(1)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test userid character < 10 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathNumberCaseOne() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "12345678"))
                .andExpect(jsonPath("$.message", is("Input userid Numeric 10 character")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test userid character > 10 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathNumberCaseTwo() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "12345678901"))
                .andExpect(jsonPath("$.message", is("Input userid Numeric 10 character")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test String character == 10 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathStringCaseOne() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "abcdefghij"))
                .andExpect(jsonPath("$.message", is("Input userid Numeric 10 character")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test String character + number == 10 GET: /users/{userId}/lotteries")
    void TestUseInvalidPathStringCombineNumber() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "abcdefg123"))
                .andExpect(jsonPath("$.message", is("Input userid Numeric 10 character")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test String character == 10 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathStringSpecialCase() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "$+-*!@$$$*"))
                .andExpect(jsonPath("$.message", is("Input userid Numeric 10 character")))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Test String character จจจจ == 10 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathStringSpecialCaseTwo() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "จจจจจจจจจจ"))
                .andExpect(jsonPath("$.message", is("Input userid Numeric 10 character")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test user id number character == 12345678.9 GET: /users/{userId}/lotteries")
    void TestUserInvalidPathNumberCaseThree() throws Exception {

        mockMvc.perform(get("/users/{userId}/lotteries", "12345678.9"))
                .andExpect(jsonPath("$.message",is("Input userid Numeric 10 character")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test user id number character < 10 POST /users/{userId}/lotteries/{ticketId}")
    void TestPostUserInvalidPathNumberCaseThree() throws Exception {

        mockMvc.perform(post("/users/{userId}/lotteries/{ticketId}", "1234567","123456"))
                .andExpect(jsonPath("$.message",is("Input userid Numeric 10 character")))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Test ticket id number character < 6 POST /users/{userId}/lotteries/{ticketId}")
    void TestPostTicketIdInvalid() throws Exception {

        mockMvc.perform(post("/users/{userId}/lotteries/{ticketId}", "1234567890","12345"))
                .andExpect(jsonPath("$.message",is("Input ticket id Numeric 6 character")))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Test number character == 12345678.9 POST /users/{userId}/lotteries/{ticketId}")
    void TestPostUserSuccess() throws Exception {

        mockMvc.perform(post("/users/{userId}/lotteries/{ticketId}", "1234567890","123456"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /users/{userId}/lotteries/{ticketId}")
    void TestWhenUserBuyTicket_Success() throws Exception {

        doReturn(1).when(userService).userBuyTicket(anyString(),anyString());

        mockMvc.perform(post("/users/{userId}/lotteries/{ticketId}", "1234567890","123456"))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /users/{userId}/lotteries/{ticketId}")
    void TestWhenUserIntValue() throws Exception {

        doReturn(1).when(userService).userBuyTicket(anyString(),anyString());

        mockMvc.perform(post("/users/{userId}/lotteries/{ticketId}", null,null))
                .andExpect(status().isNotFound());
    }
}
