package com.example.proyectofinal.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.proyectofinal.Adapters.AdapterContactsList;
import com.example.proyectofinal.Model.User;
import com.example.proyectofinal.R;

import java.util.ArrayList;


public class ContactsFragment extends Fragment {

    private ArrayList<User> listaUsers;
    private ArrayList<User> finderLista;
    private RecyclerView recyclerView;
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

    public ContactsFragment() {
        // Required empty public constructor
    }


    public void filterContacts(String filter){
        this.finderLista = new ArrayList<>();

        for (User u: this.listaUsers) {
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


        this.recyclerView =  view.findViewById(R.id.lista_contacts);
        this.finder =  view.findViewById(R.id.search_contact);

        this.finder.setOnQueryTextListener(listenerFinder);




        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        this.listaUsers = new ArrayList<User>();


        for (int x=0;x<50;x++){

            this.listaUsers.add(new User("nickname"+x,"nickname"+x+"@gmail.com"));
        }
        this.adapterContactsList = new AdapterContactsList(this.listaUsers);
        this.recyclerView.setAdapter(adapterContactsList);

        return view;
    }




}
