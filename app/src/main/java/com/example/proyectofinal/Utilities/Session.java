package com.example.proyectofinal.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.proyectofinal.Model.UserClient;

public class Session {


    private SharedPreferences preferences;

    public Session(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void setUser(UserClient user){
        preferences.edit().putString("id", user.getId()).commit();
        preferences.edit().putString("usename", user.getNickname()).commit();;
        preferences.edit().putString("email", user.getEmail()).commit();
    }



    public UserClient getUser() {
        String id = preferences.getString("id","");
        String usename = preferences.getString("usename","");
        String email = preferences.getString("email","");
        UserClient user = new UserClient();
        user.setNickname(usename);
        user.setEmail(email);
        user.setId(id);
        return user;
    }

    public void logout(){
        preferences.edit().clear().commit();
    }

}
