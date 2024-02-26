package com.kbtg.bootcamp.posttest.adminTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbtg.bootcamp.posttest.admin.AdminController;
import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import com.kbtg.bootcamp.posttest.user.user_ticket.UserTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class adminControllerTest {
    MockMvc mockMvc;


    @Mock
    private LotteryService lotteryService;


    @BeforeEach
    void setUp() {
        AdminController adminController = new AdminController(lotteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("Test Admin add Lottery to LotteryRepository")
    public void testPostData() throws Exception {
        LotteryRequest lotteryRequest = new LotteryRequest("123456",123,1);
        UserTicket userTicket = new UserTicket("123456789","ADMIN","ADD","123456","1","123");
        doReturn(userTicket).when(lotteryService).createNewLotteryByAdmin(ArgumentMatchers.any(LotteryRequest.class));
        mockMvc.perform(post("/admin/lotteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lotteryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticket", is(lotteryRequest.getTicket())));// Ensure the "ticket" list contains 3 elements
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

