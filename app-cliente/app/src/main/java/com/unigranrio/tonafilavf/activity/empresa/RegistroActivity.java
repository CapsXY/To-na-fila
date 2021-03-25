package com.unigranrio.tonafilavf.activity.empresa;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

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

public class RegistroActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_registro);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("TôNaFila! - Atendimento");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idEmpresa = UsuarioFirebase.getIdUsuario();
        idUsuario = UsuarioFirebase.getIdUsuario();

        recyclerViewSolicitacoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSolicitacoes.setHasFixedSize(true);
        adapterSolicitacao = new AdapterSolicitacao(solicitacoes);
        recyclerViewSolicitacoes.setAdapter(adapterSolicitacao);

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

    private void recuperarSolicitacoes() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Nenhum registro...")
                .setCancelable(true)
                .build();
        dialog.show();

        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes")
                .child(idUsuario);

        Query solicitacaoPesquisa = solicitacaoRef
                .orderByChild("status")
                .equalTo("Chamado");

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
        recyclerViewSolicitacoes = findViewById(R.id.recyclerViewSolicitacoes);
    }
}
