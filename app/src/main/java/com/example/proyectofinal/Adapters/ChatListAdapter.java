package com.example.proyectofinal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectofinal.Model.Chat;
import com.example.proyectofinal.R;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Chat> chats;

    public ChatListAdapter(Context context, ArrayList<Chat> chats) {
        this.context = context;
       this.chats = chats;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.chat_item, parent, false);
        Chat chat = chats.get(position);

        TextView title = (TextView) v.findViewById(R.id.titleChat);
        title.setText(chat.getReceptor().getNickname());

        TextView description = (TextView) v.findViewById(R.id.lastMessage);
        description.setText(chat.getReceptor().getNickname());



        return v;
    }
}
