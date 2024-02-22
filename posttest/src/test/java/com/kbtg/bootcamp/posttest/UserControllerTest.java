package com.kbtg.bootcamp.posttest;

import com.kbtg.bootcamp.posttest.user.User;
import com.kbtg.bootcamp.posttest.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    MockMvc mvc;


    @Mock
    private UserRepository userRepository;


    @Test
    void savedUserHasRegistrationDate() {
        User user = new User();
        user.setRoles("USER");
        user.setEncoderpassword("09876543");
        user.setUserId("134567543");

        userRepository.save(user);
        // Retrieve the saved user
        Optional<User> savedUserOptional = userRepository.findById(user.getId().longValue());

        // Assert that the saved user exists
        assertTrue(savedUserOptional.isPresent(), "User should be saved");

        // Get the saved user
        User savedUser = savedUserOptional.get();

        // Assert that the saved user has a registration date

    }


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
