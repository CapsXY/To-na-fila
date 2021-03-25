package com.unigranrio.tonafilavf.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.model.DadosSolicitacao;
import com.unigranrio.tonafilavf.model.Solicitacao;

import java.util.ArrayList;
import java.util.List;

public class AdapterSolicitacao extends RecyclerView.Adapter<AdapterSolicitacao.MyViewHolder> {

    private List<Solicitacao> solicitacoes;

    public AdapterSolicitacao(List<Solicitacao> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_solicitacao, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Solicitacao solicitacao = solicitacoes.get(i);
        holder.textViewNomeCliente.setText(solicitacao.getNomeUsuario());
        holder.textViewTelefoneCliente.setText(solicitacao.getTelefoneUsuario());
        holder.textViewObservacaoCliente.setText(solicitacao.getObservacao());
        holder.textViewStatusCliente.setText(solicitacao.getStatus());
        holder.textViewNomeEmpresa.setText(solicitacao.getNomeEmpresa());
        holder.textViewEmailCliente.setText(solicitacao.getEmail());

        List<DadosSolicitacao> dadosFila = new ArrayList<>();
        dadosFila = solicitacao.getDadosFila();
        String descricaoFila = "";

        int numeroDados = 1;
        Double total = 0.0;
        for (DadosSolicitacao dadosSolicitacao : dadosFila) {

            int quantidade = dadosSolicitacao.getQuantidade();
            Double code = dadosSolicitacao.getCode();
            total += (quantidade * code);

            String nome = dadosSolicitacao.getNomeFila();
            descricaoFila += quantidade + "";

        }
        descricaoFila += "";
        holder.textViewQuantidadeCliente.setText(descricaoFila);

        int categoria = solicitacao.getOpcao();
        String escolhaCategoria = categoria == 0 ? "PNE / Fumante" : "Nenhum";
        holder.textViewOpcaoCliente.setText(escolhaCategoria);
    }

    @Override
    public int getItemCount() {
        return solicitacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNomeCliente;
        TextView textViewTelefoneCliente;
        TextView textViewQuantidadeCliente;
        TextView textViewOpcaoCliente;
        TextView textViewObservacaoCliente;
        TextView textViewStatusCliente;
        TextView textViewNomeEmpresa;
        TextView textViewEmailCliente;


        public MyViewHolder(View itemView) {
            super(itemView);

            textViewNomeCliente = itemView.findViewById(R.id.textViewNomeCliente);
            textViewTelefoneCliente = itemView.findViewById(R.id.textViewTelefoneCliente);
            textViewQuantidadeCliente = itemView.findViewById(R.id.textViewQuantidadeCliente);
            textViewOpcaoCliente = itemView.findViewById(R.id.textViewOpcaoCliente);
            textViewObservacaoCliente = itemView.findViewById(R.id.textViewObservacaoCliente);
            textViewStatusCliente = itemView.findViewById(R.id.textViewStatusCliente);
            textViewNomeEmpresa = itemView.findViewById(R.id.textViewNomeEmpresa);
            textViewEmailCliente = itemView.findViewById(R.id.textViewEmailCliente);
        }
    }
}
