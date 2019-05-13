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


import com.example.proyectofinal.Adapters.AdapterContactsList;
import com.example.proyectofinal.Model.Contact;
import com.example.proyectofinal.ProfileUserActivity;
import com.example.proyectofinal.R;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    private ArrayList<Contact> listaContact;
    private ArrayList<Contact> finderLista;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView finder;
    private AdapterContactsList adapterContactsList;

    private SearchView.OnQueryTextListener listenerFinder = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            filterContacts(finder.getQuery().toString());
            return false;
        }
    };

    private View.OnClickListener listenerClickChat = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ProfileUserActivity.class);
            intent.putExtra("nickname",listaContact.get(recyclerView.getChildAdapterPosition(view)).getNickname());
            startActivity(intent);
          //   Toast.makeText(getContext(),listaContact.get(recyclerView.getChildAdapterPosition(view)).getNickname(),Toast.LENGTH_SHORT).show();
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

        this.listaContact = new ArrayList<com.example.proyectofinal.Model.Contact>();


        for (int x=0;x<50;x++){

            this.listaContact.add(new Contact("nickname"+x,"nickname"+x+"@gmail.com",x+"1234566"));
        }
        this.adapterContactsList = new AdapterContactsList(this.listaContact);
        this.adapterContactsList.setOnClickListener(listenerClickChat);

        this.recyclerView.setAdapter(adapterContactsList);

        return view;
    }

    




}
