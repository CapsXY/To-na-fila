package com.unigranrio.tonafilavf.activity.cliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.adapter.AdapterSolicitacao;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavf.helper.UsuarioFirebase;
import com.unigranrio.tonafilavf.listener.RecyclerItemClickListener;
import com.unigranrio.tonafilavf.model.Solicitacao;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class FilaUsuarioActivity extends AppCompatActivity {

    private DatabaseReference firebaseRef;
    private String idEmpresa;
    private String idUsuario;
    private RecyclerView recyclerViewSolicitacoes;
    private AdapterSolicitacao adapterSolicitacao;
    private List<Solicitacao> solicitacoes = new ArrayList<>();
    private Solicitacao solicitacao;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fila_usuario);

        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idEmpresa = UsuarioFirebase.getIdUsuario();
        idUsuario = UsuarioFirebase.getIdUsuario();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("TôNaFila! - Solicitações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewSolicitacoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSolicitacoes.setHasFixedSize(true);
        adapterSolicitacao = new AdapterSolicitacao(solicitacoes);
        recyclerViewSolicitacoes.setAdapter(adapterSolicitacao);

        swipe();
        recuperarSolicitacoes();

        recyclerViewSolicitacoes.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this, recyclerViewSolicitacoes,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewSolicitacoes);

    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Sair da fila");
        alertDialog.setMessage("Você realmente deseja sair da fila?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                solicitacao = solicitacoes.get(position);
                solicitacao.removerSolicitacao();
                solicitacao.removerSolicitacaoUsuario();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FilaUsuarioActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                adapterSolicitacao.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void recuperarSolicitacoes() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Você ainda não entrou na fila...")
                .setCancelable(true)
                .build();
        dialog.show();

        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes_usuario")
                .child(idUsuario);

        Query solicitacaoPesquisa = solicitacaoRef
                .orderByChild("status")
                .equalTo("Aguardando");

        solicitacaoPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                solicitacoes.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Solicitacao solicitacao = ds.getValue(Solicitacao.class);
                        solicitacoes.add(solicitacao);
                    }
                    adapterSolicitacao.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void inicializarComponentes() {
        recyclerViewSolicitacoes = findViewById(R.id.recyclerViewFilaUsuario);
    }
}