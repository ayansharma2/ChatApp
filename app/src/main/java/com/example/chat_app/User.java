package com.example.chat_app;

public class User {
    String name,email,password,image_URL,user_ID,user_status,search;

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public User(String name, String email, String password, String image_URL, String user_ID,String user_status,String search) {
        this.name = name;
        this.email = email;
        this.image_URL=image_URL;
        this.password = password;
        this.user_ID=user_ID;
        this.user_status=user_status;
        this.search=search;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
}
