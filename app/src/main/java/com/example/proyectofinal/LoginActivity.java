package com.example.proyectofinal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Model.User;
import com.example.proyectofinal.Model.UserClient;
import com.example.proyectofinal.Utilities.BackendConection;
import com.example.proyectofinal.Utilities.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView linkSignup;
    private EditText inputNickname;
    private EditText inputPassword;
    private RequestQueue mRequestQueue;
    private Session session;
    private UserClient user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.linkSignup = (TextView) findViewById(R.id.link_signup);
        this.inputNickname = (EditText) findViewById(R.id.input_nickname);
        this.inputPassword = (EditText) findViewById(R.id.input_password);
        this.mRequestQueue = Volley.newRequestQueue(this);


        this.linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

    }

    public void loginUser(View view) {
        ProgressDialog barProgressDialog;
        barProgressDialog = new ProgressDialog(this);
        barProgressDialog.setTitle("Logeando");
        barProgressDialog.setMessage("Por favor espere ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.show();

        new Thread(new Runnable(){
            @Override
            public void run(){

                    makePetitionLogin();

                barProgressDialog.dismiss();
            }
        }).start();

    }

    private void makePetitionLogin(){
        User user  =new User();
        user.setNickname(inputNickname.getText().toString());
        user.setPassword(inputPassword.getText().toString());

        StringRequest postRequest = new StringRequest(Request.Method.POST, BackendConection.SERVER+"/login",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        validarLogin(response);
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
                params.put("nickname",user.getNickname());
                params.put("password", user.getPassword());


                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    private void validarLogin(String response) {

        try {

            JSONObject obj = new JSONObject(response);
            if(obj.has("err")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Usuario o contraseña incorrectas")
                        .setTitle("Error al logear")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                AlertDialog dialog = builder.create();
            }
            else{
                Toast.makeText(this,"Logeado",Toast.LENGTH_SHORT).show();
                this.user = new UserClient();
                this.user.setId(obj.getString("_id"));
                this.user.setNickname(obj.getString("nickname"));
                this.user.setEmail(obj.getString("email"));


                      this.initSession();
             startActivity(new Intent(this,MainActivity.class));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initSession(){
            this.session = new Session(this);
            this.session.setUser(this.user);
    }

}
