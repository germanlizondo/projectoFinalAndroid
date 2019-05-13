package com.example.proyectofinal;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Adapters.AdapterChatsList;
import com.example.proyectofinal.Adapters.AdapterListMesages;
import com.example.proyectofinal.Model.Chat;
import com.example.proyectofinal.Model.Contact;
import com.example.proyectofinal.Model.Mensaje;
import com.example.proyectofinal.Model.User;
import com.example.proyectofinal.Utilities.BackendConection;
import com.example.proyectofinal.Utilities.Session;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<Mensaje> mensajes;
    private RecyclerView recyclerView;
    private AdapterListMesages adapterListMesages;
    private EditText edittext_chatbox;
    private RequestQueue mRequestQueue;
    private String url;
    private Session session;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(BackendConection.SERVER);
        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String idchat= intent.getStringExtra("idchat");
        String receptorNickname = intent.getStringExtra("receptor");
        setTitle(receptorNickname);

        this.session = new Session(this);

        mSocket.connect();
        this.url = BackendConection.SERVER +"/get-message/"+idchat;
        this.mensajes = new ArrayList<>();

        findMessagesPetition();

     this.findMessagesPetition();
        this.recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        this.edittext_chatbox = (EditText) findViewById(R.id.edittext_chatbox);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);

        this.adapterListMesages = new AdapterListMesages(this.mensajes,session.getUser());
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
            Mensaje mensaje = new Mensaje(this.edittext_chatbox.getText().toString(),session.getUser());
            this.mensajes.add(mensaje);

            this.adapterListMesages = new AdapterListMesages(this.mensajes,session.getUser());
            this.recyclerView.setAdapter(adapterListMesages);
            this.edittext_chatbox.setText("");
            JSONObject mensajeJson = new JSONObject();
            try {
                mensajeJson.put("content",mensaje.getContent());
                mensajeJson.put("date",mensaje.getDate());
                mensajeJson.put("author",mensaje.getUser().getId());
                mSocket.emit("newmensaje",mensajeJson.toString() );
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void findMessagesPetition(){

        mRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,this.url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                parseResponseToArray(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.wtf(error.getMessage(), "utf-8");
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

    public void parseResponseToArray(JSONObject response){
        try{
            JSONObject chat = response.getJSONObject("chats");
            JSONArray mensajes = chat.getJSONArray("mensajes");
            JSONObject author;
            JSONObject mensajeJson;
            Mensaje mensajeApp;
            this.mensajes.clear();

            for(int i=0;i<mensajes.length();i++){
                mensajeApp = new Mensaje();
                mensajeJson   = mensajes.getJSONObject(i);
                author = mensajeJson.getJSONObject("author");
                mensajeApp.setContent(mensajeJson.getString("content"));
                mensajeApp.setDate(mensajeJson.getString("date"));
                mensajeApp.setUser(new User(author.getString("nickname"),author.getString("email"),
                        author.getString("_id")));

                this.mensajes.add(mensajeApp);

            }

            this.adapterListMesages = new AdapterListMesages(this.mensajes,session.getUser());
            this.recyclerView.setAdapter(adapterListMesages);
            this.edittext_chatbox.setText("");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
