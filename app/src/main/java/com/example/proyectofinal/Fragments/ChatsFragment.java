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

import com.example.proyectofinal.Adapters.AdapterChatsList;
import com.example.proyectofinal.Adapters.AdapterContactsList;
import com.example.proyectofinal.ChatActivity;
import com.example.proyectofinal.Model.Chat;
import com.example.proyectofinal.Model.Contact;
import com.example.proyectofinal.R;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    private ArrayList<Chat> listaChats;
    private ArrayList<Chat> filterChats;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Intent intentChatActivity;
    private SearchView finder;
    private  AdapterChatsList adapterChatsList;

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
            getActivity().startActivity(intentChatActivity);
           // Toast.makeText(getContext(),listaChats.get(recyclerView.getChildAdapterPosition(view)),Toast.LENGTH_SHORT).show();
        }
    };


    public ChatsFragment() {
        // Required empty public constructor
    }

    public void filterChats(String filter){
        this.filterChats = new ArrayList<Chat>();

        for (Chat u: this.listaChats) {
            if(u.getReceptor().getNickname().startsWith(filter)){
                this.filterChats.add(u);
            }
        }

        this.adapterChatsList = new AdapterChatsList(this.filterChats);

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
                Toast.makeText(getActivity(),"Refrescado",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        this.recyclerView = (RecyclerView) view.findViewById(R.id.lista_chats);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        this.finder = view.findViewById(R.id.search_chat);
        this.finder.setOnQueryTextListener(listenerFinder);



        this.listaChats = new ArrayList<Chat>();

        for (int x=0;x<50;x++){
            this.listaChats.add(new Chat(new Contact("german"+x,"email"+x)));
        }
        this.adapterChatsList = new AdapterChatsList(this.listaChats);

        adapterChatsList.setOnClickListener(listenerClickChat);
        recyclerView.setAdapter(adapterChatsList);

        return view;
    }


}
