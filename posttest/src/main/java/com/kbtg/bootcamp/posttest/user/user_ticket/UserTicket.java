package com.kbtg.bootcamp.posttest.user.user_ticket;


import jakarta.persistence.*;

@Entity
@Table(name = "user_ticket")
public class UserTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "userid", unique = true, nullable = false, length = 255)
    private String userid;


    private String roles;
    private String user_action;
    private String ticket;

    private String amount;


    private String price;

    public UserTicket()
    {

    }
    public UserTicket(String userid,String role,String user_action,String ticket, String amount, String price)
    {
        this.userid =userid;
        this.roles =role;
        this.user_action = user_action;
        this.ticket = ticket;
        this.amount = amount;
        this.price =price;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }




    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getUser_action() {
        return user_action;
    }

    public void setUser_action(String user_action) {
        this.user_action = user_action;
    }

    public String getRole() {
        return roles;
    }

    public void setRole(String role) {
        this.roles = role;
    }
}
