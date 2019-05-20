package com.example.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Adapters.AdapterListMesages;
import com.example.proyectofinal.Model.Mensaje;
import com.example.proyectofinal.Model.User;
import com.example.proyectofinal.Utilities.BackendConection;
import com.example.proyectofinal.Utilities.Session;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
    private String idchat;
    private String idReceptor;
    private String receptorNickname;
    private PublicKey clavePublica;
    private PrivateKey clavePrivada;
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
        this.idchat= intent.getStringExtra("idchat");
        this.idReceptor= intent.getStringExtra("idReceptor");

                this.receptorNickname = intent.getStringExtra("receptor");
        setTitle(receptorNickname);

        this.session = new Session(this);



        this.url = BackendConection.SERVER +"/get-message/"+this.idchat;
        this.mensajes = new ArrayList<>();





    }

    @Override
    public void onResume(){
        super.onResume();
        mSocket.connect();
        mSocket.emit("join-room",this.idchat);
        mSocket.on("new-message", onNewMessage);

        findKeys();

        this.findMessagesPetition();
        this.recyclerView = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        this.edittext_chatbox = (EditText) findViewById(R.id.edittext_chatbox);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        this.recyclerView.setLayoutManager(linearLayoutManager);

        this.adapterListMesages = new AdapterListMesages(this.mensajes,session.getUser());
        this.recyclerView.setAdapter(adapterListMesages);

    }

    @Override
    public void onPause(){
        super.onPause();
        mSocket.emit("leave-room",this.idchat);
        mSocket.close();

        this.clavePublica = null;
        this.clavePrivada = null;

    }


    public void abrirUbicacion(View view) {
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("idUser",this.idReceptor);
        intent.putExtra("nickname",receptorNickname);
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
            JSONObject data = new JSONObject();

            JSONObject mensajeJson = new JSONObject();
            JSONObject authorJson = new JSONObject();
            try {

                data.put("room",this.idchat);

                authorJson.put("id",this.session.getUser().getId());
                authorJson.put("email",this.session.getUser().getEmail());
                authorJson.put("nickname",this.session.getUser().getNickname());

                mensajeJson.put("content",Rsa.encrypt(this.clavePublica,mensaje.getContent()));
                mensajeJson.put("date",mensaje.getDate());
                mensajeJson.put("author",authorJson);
                data.put("message",mensajeJson);
                mSocket.emit("new-mensaje",data);

            } catch (Exception e) {
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
                mensajeApp.setContent(Rsa.decrypt(this.clavePrivada,mensajeJson.getString("content")));
                mensajeApp.setDate(mensajeJson.getString("date"));
                mensajeApp.setUser(new User(author.getString("nickname"),author.getString("email"),
                        author.getString("_id")));

                this.mensajes.add(mensajeApp);

            }

            this.adapterListMesages = new AdapterListMesages(this.mensajes,session.getUser());
            this.recyclerView.setAdapter(adapterListMesages);
            this.edittext_chatbox.setText("");

        }catch (Exception e){
            e.printStackTrace();

        }
    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    addMessageFromSocket(data);
                    Log.w("DATA SOCKET: ",data.toString());
               //     Toast.makeText(ChatActivity.this,data.toString(),Toast.LENGTH_SHORT).show();
                }


            });

        }
    };

    private void addMessageFromSocket(JSONObject data) {
        Mensaje mensaje = new Mensaje();
        User author =new User();
        try {
            JSONObject authorJson = data.getJSONObject("author");
            author.setNickname(authorJson.getString("nickname"));
            mensaje.setContent(data.getString("content"));
            mensaje.setDate(data.getString("date"));
            mensaje.setUser(author);
            this.mensajes.add(mensaje);
            this.adapterListMesages = new AdapterListMesages(this.mensajes,session.getUser());
            this.recyclerView.setAdapter(adapterListMesages);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void findKeys(){
        mRequestQueue = Volley.newRequestQueue(this);
        String url = BackendConection.SERVER+"/get-key/"+this.idchat;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                getKeyJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.wtf(error.getMessage(), "utf-8");
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

    public void getKeyJson(JSONObject response){

        try {

            JSONObject chat = response.getJSONObject("chat");
            String publicKeyString = chat.getString("publicKey");
            String privateKeyString = chat.getString("privateKey");
            this.clavePrivada = Rsa.getPrivateKey(privateKeyString);
            this.clavePublica = Rsa.getPublicKey(publicKeyString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
