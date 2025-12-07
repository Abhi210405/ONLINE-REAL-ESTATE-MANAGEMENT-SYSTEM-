package com.msp.realestate;

public class User {
    public int id;
    public String username;
    public String email;
    public String phone;
    public String role;

    public User(int id, String username, String email, String phone, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
