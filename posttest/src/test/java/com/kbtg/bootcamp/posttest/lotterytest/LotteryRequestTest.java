package com.kbtg.bootcamp.posttest.lotterytest;

import com.kbtg.bootcamp.posttest.lottery.LotteryRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class LotteryRequestTest {

    @Mock
    LotteryRequest lotteryRequest;
    @Autowired
    private LocalValidatorFactoryBean validator1;
    @Autowired
    private Validator validator;
//    @Autowired private LotteryRequest lotteryRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    @Test
    public void testValidUser() {


        LotteryRequest lotteryRequest = new LotteryRequest();
        lotteryRequest.setTicket("123456");
        lotteryRequest.setPrice(100);
        lotteryRequest.setAmount(5);

        // Validate the LotteryRequest
        Set<ConstraintViolation<LotteryRequest>> violations = validator.validate(lotteryRequest);

        // Assert that there are no violations
        assertThat(violations).isEmpty();

    }


    @Test
    @DisplayName("Test admin set All value is null")
    public void testInvalidLotteryRequestallValueIsNull() {
        // Create an invalid LotteryRequest with null values
        LotteryRequest lotteryRequest = new LotteryRequest();

        // Validate the LotteryRequest
        Set<ConstraintViolation<LotteryRequest>> violations = validator.validate(lotteryRequest);

        // Assert that there are violations
        assertThat(violations).hasSize(3);

        // Assert individual violations
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Ticket must not be null",
                "Price must not be null",
                "amount must not be null"
        );
    }

    @Test
    @DisplayName("Test admin set All value is Min Price")
    public void testInvalidLotteryRequestMinPrice() {
        // Create an invalid LotteryRequest with negative values
        LotteryRequest lotteryRequest = new LotteryRequest();
        lotteryRequest.setTicket("123456"); // Not 6 characters long
        lotteryRequest.setPrice(1);  // Negative price
        lotteryRequest.setAmount(1); // Negative amount

        // Validate the LotteryRequest
        Set<ConstraintViolation<LotteryRequest>> violations = validator.validate(lotteryRequest);

        // Assert that there are violations
        assertThat(violations).hasSize(1);

        // Assert individual violations
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Price must not exceed the min allowed value Min:10"
        );
    }

    @Test
    @DisplayName("Test admin set All value is Max Int")
    public void testInvalidLotteryRequestAllNumberisMaxInt() {
        // Create an invalid LotteryRequest with negative values
        LotteryRequest lotteryRequest = new LotteryRequest();
        lotteryRequest.setTicket(""); // Not 6 characters long
        lotteryRequest.setPrice(Integer.MAX_VALUE);  // Negative price
        lotteryRequest.setAmount(Integer.MAX_VALUE); // Negative amount

        // Validate the LotteryRequest
        Set<ConstraintViolation<LotteryRequest>> violations = validator.validate(lotteryRequest);

        // Assert that there are violations
        assertThat(violations).hasSize(4);

        // Assert individual violations
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Ticket must be exactly 6 characters long",
                "Ticket must contain only numeric characters",
                "Price must not exceed the maximum allowed value Max:300",
                "amount must not exceed the maximum allowed value Max:300"
        );
    }

    @Test
    @DisplayName("Test admin set All value is Min Amount")
    public void testInvalidLotteryRequestMinAmount() {
        // Create an invalid LotteryRequest with negative values
        LotteryRequest lotteryRequest = new LotteryRequest();
        lotteryRequest.setTicket("123456");
        lotteryRequest.setPrice(10);  // Negative price
        lotteryRequest.setAmount(0); // Negative amount

        // Validate the LotteryRequest
        Set<ConstraintViolation<LotteryRequest>> violations = validator.validate(lotteryRequest);

        // Assert that there are violations
        assertThat(violations).hasSize(1);

        // Assert individual violations
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "amount must be positive"
        );
    }


    @Test
    @DisplayName("Test admin set All value is Negative")
    public void testInvalidLotteryRequestAllnegative() {
        // Create an invalid LotteryRequest with negative values
        LotteryRequest lotteryRequest = new LotteryRequest();
        lotteryRequest.setTicket("123456"); // Not 6 characters long
        lotteryRequest.setPrice(-1);  // Negative price
        lotteryRequest.setAmount(-1); // Negative amount

        // Validate the LotteryRequest
        Set<ConstraintViolation<LotteryRequest>> violations = validator.validate(lotteryRequest);

        // Assert that there are violations
        assertThat(violations).hasSize(3);

        // Assert individual violations
        assertThat(violations).extracting("message").containsExactlyInAnyOrder(
                "Price must not exceed the min allowed value Min:10",
                "Price must be positive",
                "amount must be positive"
        );
    }

}