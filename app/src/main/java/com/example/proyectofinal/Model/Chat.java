package com.example.proyectofinal.Model;

import java.util.ArrayList;

public class Chat {

    private UserClient transmitor;
    private Contact receptor;
    private ArrayList<Mensaje> mensajes;

    public Chat() {
    }

    public Chat(Contact receptor) {
        this.receptor = receptor;
    }

    public UserClient getTransmitor() {
        return transmitor;
    }

    public void setTransmitor(UserClient transmitor) {
        this.transmitor = transmitor;
    }

    public Contact getReceptor() {
        return receptor;
    }

    public void setReceptor(Contact receptor) {
        this.receptor = receptor;
    }

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
