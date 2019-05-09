package com.example.proyectofinal;

import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


    private SearchView.OnQueryTextListener listenerFinder = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            findUsersPetition(s);
           // Toast.makeText(AddUserActivity.this,s,Toast.LENGTH_SHORT).show();
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

        for (int x=0;x<50;x++){

            this.listaContact.add(new Contact("nickname"+x,"nickname"+x+"@gmail.com"));

        }


        this.adapterContactsList = new AdapterContactsList(this.listaContact);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        this.recyclerView.setAdapter(adapterContactsList);
    }

    public void findUsersPetition(String nickname){

        mRequestQueue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.wtf(response.toString());
                parseResponseToArray(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.wtf(error.getMessage(), "utf-8");
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    public void parseResponseToArray(String response){
        Toast.makeText(this,response + "hola",Toast.LENGTH_SHORT).show();

    }


}
