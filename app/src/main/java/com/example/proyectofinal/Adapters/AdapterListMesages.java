package com.example.proyectofinal.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectofinal.Model.Mensaje;
import com.example.proyectofinal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterListMesages extends RecyclerView.Adapter<AdapterListMesages.MesagesViewHolder> {

    private ArrayList<Mensaje> mensajes;
    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;


    public AdapterListMesages(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    @NonNull
    @Override
    public MesagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == TYPE_ONE){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mesage_sended_item,null,false);
            return new MesagesViewHolder(view);
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mesage_recived_item,null,false);
            return new MesagesViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MesagesViewHolder mesagesViewHolder, int i) {
        mesagesViewHolder.asignarMesage(this.mensajes.get(i));
    }

    @Override
    public int getItemCount() {
        return this.mensajes.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = this.mensajes.get(position);

        Log.w("HELLO THERE: ",mensaje.getContent());
        if (mensaje.getUser().getNickname().equals("german")) {
            return TYPE_ONE;
        }  else {
            return TYPE_TWO;
        }
    }

    public class MesagesViewHolder extends RecyclerView.ViewHolder {
        private TextView usuario;
        private TextView content;
        private TextView date;
        public MesagesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.usuario = itemView.findViewById(R.id.text_message_name);
            this.content = itemView.findViewById(R.id.text_message_body);
            this.date = itemView.findViewById(R.id.text_message_time);
        }

        public void asignarMesage(Mensaje mensaje) {

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            this.usuario.setText(mensaje.getUser().getNickname());
            this.content.setText(mensaje.getContent());
            this.date.setText(format.format(mensaje.getDate()));
        }
    }
}
