package com.example.proyectofinal.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.proyectofinal.Adapters.ChatListAdapter;

import com.example.proyectofinal.R;



public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View myFragmentView = inflater.inflate(R.layout.fragment_chats, container, false);

        RecyclerView listaChats = (RecyclerView) myFragmentView.findViewById(R.id.listachats);

        listaChats.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        listaChats.setLayoutManager(llm);

        return inflater.inflate(R.layout.fragment_chats, container, false);
    }


}
