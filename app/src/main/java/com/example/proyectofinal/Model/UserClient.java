package com.example.proyectofinal.Model;

import java.util.ArrayList;

public class UserClient extends User {


    private String password;
    private ArrayList<Chat> chats;
    private ArrayList<Contact> contacts;

    public UserClient() {
    }

    public UserClient(String nickname, String correo) {
    super(nickname,correo);
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

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
