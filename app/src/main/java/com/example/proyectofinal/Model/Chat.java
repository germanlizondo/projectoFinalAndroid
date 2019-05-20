package com.example.proyectofinal.Model;

import com.example.proyectofinal.Rsa;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Chat {

    private String id;
    private Contact receptor;
    private ArrayList<Mensaje> mensajes;
    private PublicKey publicKey;
    private PrivateKey privateKey;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = Rsa.getPublicKey(publicKey);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = Rsa.getPrivateKey(privateKey);
    }
}
