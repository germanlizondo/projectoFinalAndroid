package com.example.proyectofinal.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Adapters.AdapterContactsList;
import com.example.proyectofinal.AddUserActivity;
import com.example.proyectofinal.Model.Contact;
import com.example.proyectofinal.ProfileUserActivity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.Utilities.BackendConection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    private ArrayList<Contact> listaContact;
    private ArrayList<Contact> finderLista;
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
            Intent intent = new Intent(getActivity(), ProfileUserActivity.class);
            intent.putExtra("nickname",listaContact.get(recyclerView.getChildAdapterPosition(view)).getNickname());
            intent.putExtra("email",listaContact.get(recyclerView.getChildAdapterPosition(view)).getEmail());
            intent.putExtra("id",listaContact.get(recyclerView.getChildAdapterPosition(view)).getId());
            startActivity(intent);
            //   Toast.makeText(getContext(),listaContact.get(recyclerView.getChildAdapterPosition(view)).getNickname(),Toast.LENGTH_SHORT).show();
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



    public ContactsFragment() {
        // Required empty public constructor
    }


    public void filterContacts(String filter){
        this.finderLista = new ArrayList<com.example.proyectofinal.Model.Contact>();

        for (Contact u: this.listaContact) {
            if(u.getNickname().startsWith(filter)){
                this.finderLista.add(u);
            }
        }

        this.adapterContactsList = new AdapterContactsList(this.finderLista);
        this.recyclerView.setAdapter(adapterContactsList);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        this.swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(),"Refrescado",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        this.recyclerView =  view.findViewById(R.id.lista_contacts);
        this.finder =  view.findViewById(R.id.search_user);

        this.finder.setOnQueryTextListener(listenerFinder);




        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));





        return view;
    }








    public void findUsersPetition(String nickname){
        JSONObject jsonBody;
        String urlEnvio = url+"/"+nickname;
        mRequestQueue = Volley.newRequestQueue(getContext());
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
                contact.setId(contactjson.getString("_id"));
                listaContact.add(contact);
            }
            adapterContactsList = new AdapterContactsList(listaContact);
            this.adapterContactsList.setOnClickListener(listenerClickChat);
            recyclerView.setAdapter(adapterContactsList);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    




}
