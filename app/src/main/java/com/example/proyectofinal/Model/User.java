package com.example.proyectofinal.Model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class User implements Serializable {

    private String id;
    private String nickname;
    private String email;
    private String password;
    private String img ="";



    public User() {
    }

    public User(String nickname, String email,String id) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

    public String encriptarSha256(String text){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuffer sb = new StringBuffer();

            for(byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = this.encriptarSha256(password);
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
