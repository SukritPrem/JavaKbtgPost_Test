package com.kbtg.bootcamp.posttest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {

    private static final String END_POINT_PATH = "/user";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Test path /{userId}/lotteries/{ticketId} when /00/lotteries/00")
    public void testAddShouldReturn400BadRequest() throws Exception {
        User newUser = new User();

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/0/lotteries/Hello")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
