package com.example.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.proyectofinal.Services.DownLoadImageTask;
import com.example.proyectofinal.Utilities.BackendConection;
import com.example.proyectofinal.Utilities.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ProfileUserActivity extends AppCompatActivity {
    private Contact contacto;
    private TextView contactname;
    private TextView emailtext;
    private Button buttonChat;
    private Session session;
    private ImageView imagenprofile;
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
        this.session = new Session(this);
        this.contactname = (TextView) findViewById(R.id.contactname);
        this.emailtext = (TextView) findViewById(R.id.emailtext);
        this.buttonChat = (Button)findViewById(R.id.buttonChat);
        this.imagenprofile = (ImageView)findViewById(R.id.imagenprofile);

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
            System.out.println(response.toString());
            JSONObject contactjson = response.getJSONObject("user");


                this.contacto.setNickname(contactjson.getString("nickname"));
                this.contacto.setEmail(contactjson.getString("email"));
                this.contacto.setId(contactjson.getString("_id"));

                if(contactjson.has("img")){
                    String imgURL  = BackendConection.SERVER+"/images/"+contactjson.getString("img");

                    new DownLoadImageTask(imagenprofile).execute(imgURL);

                }



        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    public void crearChat(View view){

        this.petitcionCrearChat(this.contacto.getId());
        startActivity(new Intent(this,MainActivity.class));

    }

    public void petitcionCrearChat(String id){
        StringRequest postRequest = new StringRequest(Request.Method.POST, BackendConection.SERVER+"/new-chat",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("transmitor",session.getUser().getId());
                params.put("receptor",contacto.getId());


                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }


}
