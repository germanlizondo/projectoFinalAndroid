package com.example.proyectofinal.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectofinal.Model.Chat;
import com.example.proyectofinal.R;

import java.util.ArrayList;

public class AdapterChatsList extends RecyclerView.Adapter<AdapterChatsList.ChatViewHolder> implements View.OnClickListener {

    private ArrayList<Chat> listaChat;
    private View.OnClickListener listener;

    public AdapterChatsList(ArrayList<Chat> listaChat) {
        this.listaChat = listaChat;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item,null,false);

        view.setOnClickListener(this);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        chatViewHolder.asignarChat(this.listaChat.get(i));

    }

    @Override
    public int getItemCount() {
        return this.listaChat.size();
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

        TextView titleChat;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleChat = itemView.findViewById(R.id.titleChat);


        }

        public void asignarChat(Chat s) {
            this.titleChat.setText(s.getReceptor().getNickname());
        }
    }
}
