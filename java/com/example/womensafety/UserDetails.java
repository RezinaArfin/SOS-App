package com.example.womensafety;

public class UserDetails {

    private String UserId,Fullname,Username,Email,Phone_num,Password;


    public UserDetails() {
    }

    public UserDetails(String userId, String fullname, String username, String email, String phone_num, String password) {
        UserId = userId;
        Fullname = fullname;
        Username = username;
        Email = email;
        Phone_num = phone_num;
        Password = password;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone_num() {
        return Phone_num;
    }

    public void setPhone_num(String phone_num) {
        Phone_num = phone_num;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
