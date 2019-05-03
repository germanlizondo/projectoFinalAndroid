package com.example.proyectofinal.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinal.Adapters.AdapterChatsList;
import com.example.proyectofinal.ChatActivity;
import com.example.proyectofinal.R;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    private ArrayList<String> listaChats;
    private RecyclerView recyclerView;
    private Intent intentChatActivity;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.lista_chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        this.listaChats = new ArrayList<>();

        for (int x=0;x<50;x++){
            this.listaChats.add("HELLO THERE: "+x);
        }
        AdapterChatsList adapterChatsList = new AdapterChatsList(this.listaChats);

        adapterChatsList.setOnClickListener(listenerClickChat);
        recyclerView.setAdapter(adapterChatsList);

        return view;
    }


}
