package com.alvarado.chadecat_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MissatgeAdapterFirestore extends RecyclerView.Adapter<MissatgeAdapterFirestore.MyViewHolder>{

    Context ctx;
    ArrayList<Missatges> msgArrayList;

    // Constructor que inicialitza l'Adapter del RecyclerView. Li passarem l'ArrayList
    // que conté les dades dels elements.
    public MissatgeAdapterFirestore(Context ctx, ArrayList<Missatges> msgArrayList) {
        this.ctx = ctx;
        this.msgArrayList = msgArrayList;
    }

    // Crea les noves vistes dels elements del RecyclerView. Aquest mètode
    // és cridat pel LayoutManager.
    @NonNull
    @Override
    public MissatgeAdapterFirestore.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(ctx).inflate(R.layout.item_missatge, parent, false);

        return new MyViewHolder(v);
    }

    // Mètode que dóna contingut a les vistes dels elements del RecyclerView. Aquest
    // mètode el crida el LayoutManager.
    @Override
    public void onBindViewHolder(@NonNull MissatgeAdapterFirestore.MyViewHolder holder, int position) {

        Missatges msg = msgArrayList.get(position);

        holder.nomUsuari.setText(msg.getNom());
        holder.missatgeUsuari.setText(msg.getMissatge());

    }


    // Mètode que retorna el nombre d'elements del DataSet que mostrem a la llista.
    // Aquest mètode el crida el LayoutManager.
    @Override
    public int getItemCount() {
        return msgArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomUsuari, missatgeUsuari;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomUsuari = itemView.findViewById(R.id.tvNomUsuari);
            missatgeUsuari = itemView.findViewById(R.id.tvMissatgeUsuari);
        }
    }
}
