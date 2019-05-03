package com.example.proyectofinal.Model;

import java.util.ArrayList;

public class Chat {

    private User transmitor;
    private User receptor;
    private ArrayList<Mensaje> mensajes;

    public Chat() {
    }


    public User getTransmitor() {
        return transmitor;
    }

    public void setTransmitor(User transmitor) {
        this.transmitor = transmitor;
    }

    public User getReceptor() {
        return receptor;
    }

    public void setReceptor(User receptor) {
        this.receptor = receptor;
    }

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
