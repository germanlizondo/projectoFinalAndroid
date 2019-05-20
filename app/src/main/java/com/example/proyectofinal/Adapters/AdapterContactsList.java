package com.example.proyectofinal.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofinal.Model.Contact;
import com.example.proyectofinal.R;
import com.example.proyectofinal.Services.DownLoadImageTask;

import java.util.ArrayList;

public class AdapterContactsList extends RecyclerView.Adapter<AdapterContactsList.ChatViewHolder> implements View.OnClickListener {

    private ArrayList<Contact> listaContacts;
    private View.OnClickListener listener;

    public AdapterContactsList(ArrayList<Contact> listaContacts) {
        this.listaContacts = listaContacts;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item,null,false);
        view.setOnClickListener(this);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {

        chatViewHolder.asignarChat(this.listaContacts.get(i));
    }

    @Override
    public int getItemCount() {
        return this.listaContacts.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView nickname;
        TextView email;
        ImageView person_photo;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nickname = itemView.findViewById(R.id.nickname);
            this.email = itemView.findViewById(R.id.email);
            this.person_photo = itemView.findViewById(R.id.person_photo);



        }

        public void asignarChat(Contact s) {
            this.nickname.setText(s.getNickname());
            this.email.setText(s.getEmail());
            if(!s.getImg().equals("")){
                new DownLoadImageTask(this.person_photo).execute(s.getImg());
            }
        }
    }
}
