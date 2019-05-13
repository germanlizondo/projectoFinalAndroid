package com.example.proyectofinal.Model;

import java.util.ArrayList;

public class Chat {

    private Contact receptor;
    private ArrayList<Mensaje> mensajes;

    public Chat() {
    }

    public Chat(Contact receptor) {
        this.receptor = receptor;
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
