package com.example.proyectofinal.Model;

import java.util.Date;

public class Mensaje {
    private String content;
    private User user;
    private boolean enviado;
    private boolean recibido;
    private Date date;

    public Mensaje() {
    }

    public Mensaje(String content, User user) {
        this.content = content;
        this.user = user;
        this.date = new Date();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public boolean isRecibido() {
        return recibido;
    }

    public void setRecibido(boolean recibido) {
        this.recibido = recibido;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
