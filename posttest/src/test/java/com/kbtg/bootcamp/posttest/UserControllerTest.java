package com.kbtg.bootcamp.posttest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbtg.bootcamp.posttest.user.UserController;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;

//    @Test

//    @DisplayName("Test path /{userId}/lotteries/{ticketId} when /00/lotteries/00")
//    public void testAddShouldReturn400BadRequest() throws Exception {
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
