package com.kbtg.bootcamp.posttest.user;


import java.util.List;

public class ReturnResultAllToUser {

    private List<String> tickets;

    private Integer cost;

    private Integer count;

    ReturnResultAllToUser()
    {

    }
    public ReturnResultAllToUser(List<String> tickets, Integer cost, Integer count)
    {
        this.tickets = tickets;
        this.cost =cost;
        this.count = count;
    }

    public List<String> getTickets() {
        return tickets;
    }

    public void setTickets(List<String> tickets) {
        this.tickets = tickets;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
