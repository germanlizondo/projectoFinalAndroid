package com.example.proyectofinal.Model;

import java.util.ArrayList;

public class User {

    private String nickname;
    private String correo;
    private String password;
    private ArrayList<Chat> chats;
    private ArrayList<User> amigos;

    public User() {
    }

    public User(String nickname, String correo) {
        this.nickname = nickname;
        this.correo = correo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public ArrayList<User> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<User> amigos) {
        this.amigos = amigos;
    }
}
