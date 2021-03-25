package com.unigranrio.tonafilavf.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.model.Fila;

import java.util.List;

public class AdapterFila extends RecyclerView.Adapter<AdapterFila.MyViewHolder> {

    private List<Fila> filas;
    private Context context;

    public AdapterFila(List<Fila> filas, Context context) {
        this.filas = filas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_fila, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Fila fila = filas.get(i);
        holder.nomeFila.setText(fila.getNomeFila());
        holder.codeFila.setText("" + fila.getCodeFila());
    }

    @Override
    public int getItemCount() {
        return filas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeFila;
        TextView codeFila;

        public MyViewHolder(View itemView) {
            super(itemView);
            nomeFila = itemView.findViewById(R.id.textViewNomeFila);
            codeFila = itemView.findViewById(R.id.textViewCodeFila);
        }
    }
}