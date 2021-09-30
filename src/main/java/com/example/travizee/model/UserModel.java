package com.example.travizee.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class UserModel {

    @Id
    @Column(name = "userid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;


    @Column(name = "email")
    private String email;


    @Column(name = "password")
    private String password;


    @Column(name = "PasswordToken")
    private String PasswordToken;

    @Column(name = "isConfirmed")
    private boolean isConfirmed = false;

    @Column(name = "username")
    private String username;



    public UserModel() {}

    public UserModel(@JsonProperty("username") String userName,
                     @JsonProperty("email") String email,
                     @JsonProperty("password") String password)
    {
        this.username = userName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getToken() {
        return PasswordToken;
    }

    public void setToken(String token) {
        this.PasswordToken = token;
    }


    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

}
