package com.example.proyectofinal.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserClient extends User implements Parcelable {


    private String password;
    private ArrayList<Chat> chats;
    private ArrayList<Contact> contacts;

    public UserClient() {
    }

    public UserClient(Parcel in){
        readFromParcel(in);
    }

    public UserClient(String nickname, String correo,String id) {
    super(nickname,correo,id);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(super.getId());
        parcel.writeString(super.getNickname());
        parcel.writeString(super.getEmail());
    }

    private void readFromParcel(Parcel in) {
        super.setId(in.readString());
        super.setNickname(in.readString());
        super.setEmail(in.readString());

    }

    public static final Parcelable.Creator<UserClient> CREATOR = new Parcelable.Creator<UserClient>(){
        public UserClient createFromParcel(Parcel in){
            return new UserClient(in);
        }
        public UserClient[] newArray(int size){
            return new UserClient[size];
        }
    };
}
