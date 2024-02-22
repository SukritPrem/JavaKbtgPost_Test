package com.kbtg.bootcamp.posttest;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


public class LotteryRequestTest {

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
        // Create a valid User object

//        LotteryRequest lotteryRequest =new LotteryRequest();
//
//        lotteryRequest.setAmount(3456);
//        lotteryRequest.setPrice(4325);
//        lotteryRequest.setTicket("123456");
//        Set<ConstraintViolation<LotteryRequest>> violations = validator.validate(lotteryRequest);
//        assertEquals("must not be blank", violations.iterator().next().getMessage());
//        // Validate the user object
//        for (ConstraintViolation<LotteryRequest> violation : violations) {
//            System.out.print(violation.getMessage());
//        }
        // Assert that there are no validation errors


    }

}
