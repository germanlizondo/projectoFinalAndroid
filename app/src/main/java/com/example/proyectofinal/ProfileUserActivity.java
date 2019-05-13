package com.example.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Adapters.AdapterContactsList;
import com.example.proyectofinal.Model.Contact;
import com.example.proyectofinal.Utilities.BackendConection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ProfileUserActivity extends AppCompatActivity {
    private Contact contacto;
    private TextView contactname;
    private TextView emailtext;

    private RequestQueue mRequestQueue;
    private String url = BackendConection.SERVER +"/get-user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        this.inicializarUIElements();
        this.initContact();

        this.asignarTextoAUI();
        this.findUsersPetition();

    }

    public void initContact(){
        contacto = new Contact();
        Intent myIntent = getIntent();
        contacto.setNickname(myIntent.getStringExtra("nickname"));
        contacto.setEmail(myIntent.getStringExtra("email"));
        contacto.setId(myIntent.getStringExtra("id"));

    }

    public void inicializarUIElements(){
        this.contactname = (TextView) findViewById(R.id.contactname);
        this.emailtext = (TextView) findViewById(R.id.emailtext);
    }

    public void asignarTextoAUI(){
        this.contactname.setText(this.contacto.getNickname());
        this.emailtext.setText(this.contacto.getEmail());
    }




    public void findUsersPetition(){
        JSONObject jsonBody;
        String urlEnvio = url+"/"+this.contacto.getId();
        mRequestQueue = Volley.newRequestQueue(this);
        try{
            jsonBody  = new JSONObject();
            jsonBody.put("id", this.contacto.getId());
        }catch (JSONException e){
            jsonBody = null;
            e.fillInStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlEnvio,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ProfileUserActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
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
            JSONObject contactjson;
            for(int i=0;i<array.length();i++){
                contactjson   = array.getJSONObject(i);
                /*
                this.contacto.setNickname(contactjson.getString("nickname"));
                this.contacto.setEmail(contactjson.getString("email"));
                this.contacto.setId(contactjson.getString("_id"));
                */
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


}
