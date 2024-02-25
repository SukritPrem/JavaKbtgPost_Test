package com.kbtg.bootcamp.posttest.lotterytest;

import com.kbtg.bootcamp.posttest.lottery.LotteryController;
import com.kbtg.bootcamp.posttest.lottery.LotteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LotteryControllerTest {
    MockMvc mockMvc;

    @Mock
    private LotteryService lotteryService;
    @BeforeEach
    void setUp() {
        LotteryController lotteryController = new LotteryController(lotteryService);
        mockMvc = MockMvcBuilders.standaloneSetup(lotteryController)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("Test Get Data From LotteryRepository")
    public void testGetData() throws Exception {
        List<String> mockLotteryData = Arrays.asList("ticket1", "ticket2", "ticket3");
        Map<String, List<String>> mockData = new HashMap<>();
        mockData.put("ticket", mockLotteryData);

        when(lotteryService.getAll_lottery()).thenReturn(mockLotteryData);

        mockMvc.perform(get("/lotteries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket", hasSize(3))) // Ensure the "ticket" list contains 3 elements
                .andExpect(jsonPath("$.ticket[0]", is("ticket1"))) // Ensure the first element is "ticket1"
                .andExpect(jsonPath("$.ticket[1]", is("ticket2"))) // Ensure the second element is "ticket2"
                .andExpect(jsonPath("$.ticket[2]", is("ticket3"))); // Ensure the third element is "ticket3"
    }



//    @Test
//    public void testCreateLottery() throws Exception {
//        // Prepare a mock LotteryRequest
//        LotteryRequest lotteryRequest = new LotteryRequest(/* Provide necessary arguments */);
//
//        // Mocking the behavior of the lotteryService
//        when(lotteryService.createNewLotteryByAdmin(any(LotteryRequest.class))).thenReturn(/* Mock the expected return value */);
//
//        // Perform the POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/lotteries")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(/* Convert lotteryRequest to JSON */))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.ticket").exists());
//
//        // Verify that the method in lotteryService was called
//        verify(lotteryService, times(1)).createNewLotteryByAdmin(any(LotteryRequest.class));
//    }
}
