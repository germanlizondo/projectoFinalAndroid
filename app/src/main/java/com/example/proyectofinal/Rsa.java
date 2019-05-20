package com.example.proyectofinal;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class Rsa {

        public static PublicKey getPublicKey(String key){


            key = key.replace("-----BEGIN PUBLIC KEY-----","");
            key = key.replace("-----END PUBLIC KEY-----","");

            KeyFactory kf;

            byte[] pubKey2 = Base64.decode(key.getBytes(),Base64.DEFAULT);
            X509EncodedKeySpec ks = new X509EncodedKeySpec(pubKey2);

            try {
                kf = KeyFactory.getInstance("RSA");
                return  kf.generatePublic(ks);


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    public static PrivateKey getPrivateKey(String key){


        key = key.replace("-----BEGIN RSA PRIVATE KEY-----","");
        key = key.replace("-----END RSA PRIVATE KEY-----","");

        KeyFactory kf;

        byte[] pubKey2 = Base64.decode(key.getBytes(),Base64.DEFAULT);
        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(pubKey2);

        try {
            kf = KeyFactory.getInstance("RSA");
            return  kf.generatePrivate(ks);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(message.getBytes());

        String cipherText =  Base64.encodeToString(bytes,Base64.DEFAULT);

        return  cipherText;
    }

    public static String decrypt(PrivateKey privateKey, String messageEncrypted) throws Exception {

        System.out.println(messageEncrypted);
        byte [] encrypted =Base64.decode(messageEncrypted,Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(encrypted);;
        return new String(bytes);
    }

}
