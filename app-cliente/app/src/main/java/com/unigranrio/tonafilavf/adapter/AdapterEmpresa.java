package com.unigranrio.tonafilavf.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.model.Empresa;

import java.util.List;

public class AdapterEmpresa extends RecyclerView.Adapter<AdapterEmpresa.MyViewHolder> {

    private List<Empresa> empresas;

    public AdapterEmpresa(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_empresa, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Empresa empresa = empresas.get(i);
        holder.textViewNomeEmpresa.setText(empresa.getNome());
        holder.textViewCategoriaEmpresa.setText(empresa.getCategoria());

        String urlImagem = empresa.getUrlImagemLogo();
        Picasso.get().load(urlImagem).into(holder.imageViewEmpresa);
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewEmpresa;
        TextView textViewNomeEmpresa;
        TextView textViewCategoriaEmpresa;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewNomeEmpresa = itemView.findViewById(R.id.textViewNomeEmpresa);
            textViewCategoriaEmpresa = itemView.findViewById(R.id.textViewCategoriaEmpresa);
            imageViewEmpresa = itemView.findViewById(R.id.imageViewEmpresa);
        }
    }
}