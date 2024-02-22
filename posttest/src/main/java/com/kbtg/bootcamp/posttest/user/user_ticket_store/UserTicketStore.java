package com.kbtg.bootcamp.posttest.user.user_ticket_store;

import jakarta.persistence.*;

@Entity
@Table(name = "user_ticket_store")
public class UserTicketStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "userid", unique = true, nullable = false, length = 255)
    private String userid;

    private String ticket;

    private String amount;


    private String price;

    public UserTicketStore()
    {

    }

    public UserTicketStore(String userid,String ticket,String amount,String price )
    {
        this.userid =userid;
        this.ticket =ticket;
        this.amount =amount;
        this.price = price;
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


}
