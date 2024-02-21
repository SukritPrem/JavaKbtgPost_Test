package com.kbtg.bootcamp.posttest.lottery;


import jakarta.validation.constraints.*;

public class LotteryRequest {


    @NotNull(message = "Ticket must not be null")
    @Size(min = 6, max = 6, message = "Ticket must be exactly 6 characters long")
    @Pattern(regexp = "\\d+", message = "Ticket must contain only numeric characters")
    private String ticket;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be positive")
    @Max(value = Integer.MAX_VALUE, message = "Price must not exceed the maximum allowed value")
    private Integer price;

    @NotNull(message = "amount must not be null")
    @Positive(message = "amount must be positive")
    @Max(value = Integer.MAX_VALUE, message = "amount must not exceed the maximum allowed value")
    private Integer amount;


    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}