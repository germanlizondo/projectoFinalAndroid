package com.example.proyectofinal;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Adapters.AdapterContactsList;
import com.example.proyectofinal.Model.Contact;
import com.example.proyectofinal.Utilities.BackendConection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    private ArrayList<Contact> listaContact;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView finder;
    private AdapterContactsList adapterContactsList;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = BackendConection.SERVER +"/all-users";

    private View.OnClickListener listenerClickChat = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(AddUserActivity.this,listaContact.get(recyclerView.getChildAdapterPosition(view)).getNickname(),Toast.LENGTH_SHORT).show();
        }
    };


    private SearchView.OnQueryTextListener listenerFinder = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if(s.equals("")){
                listaContact.clear();
                adapterContactsList = new AdapterContactsList(listaContact);
                recyclerView.setAdapter(adapterContactsList);
            } else  findUsersPetition(s);
                return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        this.finder = (SearchView) findViewById(R.id.search_user);
        this.finder.setOnQueryTextListener(listenerFinder);
        this.recyclerView = (RecyclerView) findViewById(R.id.lista_users);
        this.listaContact = new ArrayList<>();

        this.mRequestQueue = Volley.newRequestQueue(this);


        this.adapterContactsList = new AdapterContactsList(this.listaContact);
        this.adapterContactsList.setOnClickListener(listenerClickChat);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        this.recyclerView.setAdapter(adapterContactsList);
    }

    public void findUsersPetition(String nickname){
        JSONObject jsonBody;
        String urlEnvio = url+"/"+nickname;
        mRequestQueue = Volley.newRequestQueue(this);
        try{
            jsonBody  = new JSONObject();
            jsonBody.put("nickname", nickname);
        }catch (JSONException e){
            jsonBody = null;
            e.fillInStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlEnvio,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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
                    JSONArray array = response.getJSONArray("users");
                    Contact contact;
                    this.listaContact.clear();
                    JSONObject contactjson;
                    for(int i=0;i<array.length();i++){
                        contact = new Contact();
                        contactjson   = array.getJSONObject(i);
                        contact.setNickname(contactjson.getString("nickname"));
                        contact.setEmail(contactjson.getString("email"));


                        listaContact.add(contact);
                    }
            adapterContactsList = new AdapterContactsList(listaContact);
            recyclerView.setAdapter(adapterContactsList);
                }catch (JSONException e){
                    e.printStackTrace();
                }
    }


}
