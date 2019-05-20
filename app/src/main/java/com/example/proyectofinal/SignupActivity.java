package com.example.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Model.User;
import com.example.proyectofinal.Utilities.BackendConection;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText input_email,input_password,input_name;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        input_name = (EditText) findViewById(R.id.input_name);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);

        this.mRequestQueue = Volley.newRequestQueue(this);

    }

    public void createAccount(View view){
        String name = input_name.getText().toString();
        String email = input_email.getText().toString();
        String password = input_password.getText().toString();

        User user = new User();
        user.setNickname(name);
        user.setEmail(email);
        user.setPassword(password);

        StringRequest postRequest = new StringRequest(Request.Method.POST, BackendConection.SERVER+"/new-user",
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
                params.put("nickname",user.getNickname());
                params.put("email", user.getEmail());
                params.put("password", user.getPassword());


                return params;
            }
        };
        mRequestQueue.add(postRequest);

        startActivity(new Intent(this,LoginActivity.class));
    }
}
