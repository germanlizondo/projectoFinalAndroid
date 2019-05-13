package com.example.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.proyectofinal.Adapters.AdapterListMesages;
import com.example.proyectofinal.Model.Mensaje;
import com.example.proyectofinal.Model.User;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<Mensaje> mensajes;
    private RecyclerView recyclerView;
    private AdapterListMesages adapterListMesages;
    private EditText edittext_chatbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.mensajes = new ArrayList<>();

        for (int x=0;x<50;x++){
            this.mensajes.add(new Mensaje("Hello there"+x,new User("obiwan","obi@gmail.com","12345678")));
        }

        this.recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        this.edittext_chatbox = (EditText) findViewById(R.id.edittext_chatbox);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);

        this.adapterListMesages = new AdapterListMesages(this.mensajes);
        this.recyclerView.setAdapter(adapterListMesages);



    }

    public void abrirUbicacion(View view) {
        Intent intent = new Intent(this,MapsActivity.class);
        this.startActivity(intent);
    }

    public void sendMessage(View view) {
        String messasge = this.edittext_chatbox.getText().toString();
        if(messasge.equals("")){
            Toast.makeText(this,"¡Escribe algo!",Toast.LENGTH_SHORT).show();
        }else{
            this.mensajes.add(new Mensaje(this.edittext_chatbox.getText().toString(),new User("german","german9@gmail.com","1234565789")));

            this.adapterListMesages = new AdapterListMesages(this.mensajes);
            this.recyclerView.setAdapter(adapterListMesages);
            this.edittext_chatbox.setText("");
        }

    }
}
