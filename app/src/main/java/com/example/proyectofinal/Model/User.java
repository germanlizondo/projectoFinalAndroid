package com.example.proyectofinal.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String nickname;
    private String email;

    public User() {
    }

    public User(String nickname, String email,String id) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
