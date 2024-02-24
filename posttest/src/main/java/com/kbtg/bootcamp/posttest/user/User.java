package com.kbtg.bootcamp.posttest.user;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profile")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userid", unique = true, nullable = false, length = 255)
    private String userid;
    @Column(name = "roles")
    private String roles;


    private String encoderpassword;
    // Constructors

    public User() {
    }


    public User(Integer id,String userid, String roles, String encoderpassword)
    {
        this.id =id;
        this.userid = userid;
        this.roles =roles;
        this.encoderpassword = encoderpassword;
    }
    // Getters and setters


    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String userId) {
        this.userid = userId;
    }


    public void setRoles(String roles) {
        this.roles = roles;
    }



    public String getRoles() {
        return roles;
    }

    public String getEncoderpassword() {
        return encoderpassword;
    }

    public void setEncoderpassword(String encoderpassword) {
        this.encoderpassword = encoderpassword;
    }

    public Integer getId() {
        return id;
    }
}