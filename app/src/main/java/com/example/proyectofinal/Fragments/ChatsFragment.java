package com.example.proyectofinal.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Adapters.AdapterChatsList;
import com.example.proyectofinal.Adapters.AdapterContactsList;
import com.example.proyectofinal.ChatActivity;
import com.example.proyectofinal.Model.Chat;
import com.example.proyectofinal.Model.Contact;
import com.example.proyectofinal.Model.UserClient;
import com.example.proyectofinal.R;
import com.example.proyectofinal.Utilities.BackendConection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    private ArrayList<Chat> listaChats;
    private ArrayList<Chat> filterChats;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Intent intentChatActivity;
    private SearchView finder;
    private  AdapterChatsList adapterChatsList;
    private String url;
    private UserClient user;
    private RequestQueue mRequestQueue;


    private SearchView.OnQueryTextListener listenerFinder = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            filterChats(finder.getQuery().toString());
            return false;
        }
    };


    private View.OnClickListener listenerClickChat = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intentChatActivity = new Intent(getActivity(), ChatActivity.class);
            intentChatActivity.putExtra("idchat",listaChats.get(recyclerView.getChildAdapterPosition(view)).getId());
            intentChatActivity.putExtra("receptor",listaChats.get(recyclerView.getChildAdapterPosition(view)).getReceptor().getNickname());
            getActivity().startActivity(intentChatActivity);
           // Toast.makeText(getContext(),listaChats.get(recyclerView.getChildAdapterPosition(view)),Toast.LENGTH_SHORT).show();
        }
    };


    public ChatsFragment() {
        // Required empty public constructor
    }

    public void filterChats(String filter){
        if(filter.equals("")){
            this.adapterChatsList = new AdapterChatsList(this.listaChats);

        }else{
            this.filterChats = new ArrayList<Chat>();

            for (Chat u: this.listaChats) {
                if(u.getReceptor().getNickname().startsWith(filter)){
                    this.filterChats.add(u);
                }
            }

            this.adapterChatsList = new AdapterChatsList(this.filterChats);


        }

        adapterChatsList.setOnClickListener(listenerClickChat);
        recyclerView.setAdapter(adapterChatsList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        this.swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findChatsPetition();
               // Toast.makeText(getActivity(),"Refrescado",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        this.user = getArguments().getParcelable("user");

        this.url = BackendConection.SERVER +"/get-chat/"+this.user.getId();
        this.recyclerView = (RecyclerView) view.findViewById(R.id.lista_chats);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        this.finder = view.findViewById(R.id.search_chat);
        this.finder.setOnQueryTextListener(listenerFinder);


        this.findChatsPetition();

        this.listaChats = new ArrayList<Chat>();

        this.findChatsPetition();

        return view;
    }


    public void findChatsPetition(){

        mRequestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,this.url,null, new Response.Listener<JSONObject>() {
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
            JSONArray arrayChats = response.getJSONArray("chats");
            JSONArray arrayUsers;
            Chat chat;
            Contact contact;
            this.listaChats.clear();
            JSONObject chatJson;
            JSONObject userJson;
            for(int i=0;i<arrayChats.length();i++){
                chat = new Chat();
                contact = new Contact();
                chatJson   = arrayChats.getJSONObject(i);
                chat.setId(chatJson.getString("_id"));

                arrayUsers = chatJson.getJSONArray("usuarios");

                for(int x=0;x<arrayUsers.length();x++){
                    userJson = arrayUsers.getJSONObject(x);

                    if(!userJson.getString("_id").equals(this.user.getId())){
                        contact.setNickname(userJson.getString("nickname"));
                        contact.setEmail(userJson.getString("email"));
                        contact.setId(userJson.getString("_id"));
                    }

                }

                chat.setReceptor(contact);

                listaChats.add(chat);
            }
            this.adapterChatsList = new AdapterChatsList(this.listaChats);

            adapterChatsList.setOnClickListener(listenerClickChat);
            recyclerView.setAdapter(adapterChatsList);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


}
