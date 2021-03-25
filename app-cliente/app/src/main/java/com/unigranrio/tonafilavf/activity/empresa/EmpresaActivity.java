package com.unigranrio.tonafilavf.activity.empresa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.activity.comum.SobreActivity;
import com.unigranrio.tonafilavf.adapter.AdapterSolicitacao;
import com.unigranrio.tonafilavf.api.NotificacaoService;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavf.helper.UsuarioFirebase;
import com.unigranrio.tonafilavf.listener.RecyclerItemClickListener;
import com.unigranrio.tonafilavf.model.Notificacao;
import com.unigranrio.tonafilavf.model.NotificacaoDados;
import com.unigranrio.tonafilavf.model.Solicitacao;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;
    private String idEmpresa;
    private RecyclerView recyclerViewSolicitacoes;
    private AdapterSolicitacao adapterSolicitacao;
    private List<Solicitacao> solicitacoes = new ArrayList<>();
    private Solicitacao solicitacao;
    private AlertDialog dialog;
    private Retrofit retrofit;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        baseUrl = "https://fcm.googleapis.com/fcm/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idEmpresa = UsuarioFirebase.getIdUsuario();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("TôNaFila! - Empresa");
        setSupportActionBar(toolbar);

        recyclerViewSolicitacoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSolicitacoes.setHasFixedSize(true);
        adapterSolicitacao = new AdapterSolicitacao(solicitacoes);
        recyclerViewSolicitacoes.setAdapter(adapterSolicitacao);

        swipeExcluir();
        swipeChamar();
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
                                enviarNotificacao();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    public void swipeExcluir() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START;
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
        alertDialog.setTitle("Excluir da fila");
        alertDialog.setMessage("Você realmente deseja excluir da fila?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                solicitacao = solicitacoes.get(position);
                solicitacao.removerSolicitacao();
                solicitacao.removerSolicitacaoUsuario();
                solicitacao.removerSolicitacaoUsuarioHistorico();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EmpresaActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                adapterSolicitacao.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void swipeChamar() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                chamarMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewSolicitacoes);

    }

    public void chamarMovimentacao(final RecyclerView.ViewHolder viewHolder1){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Chamar cliente");
        alertDialog.setMessage("Você deseja chamar o cliente?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder1.getAdapterPosition();
                Solicitacao solicitacao = solicitacoes.get(position);
                solicitacao.setStatus("Chamado");
                solicitacao.atualizarStatus();
                solicitacao.atualizarStatusUsuario();
                solicitacao.atualizarStatusUsuarioHistorico();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EmpresaActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                adapterSolicitacao.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void recuperarSolicitacoes() {

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Aguardando solicitações...")
                .setCancelable(true)
                .build();
        dialog.show();

        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes")
                .child(idEmpresa);

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
        recyclerViewSolicitacoes = findViewById(R.id.recyclerViewSolicitacoes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menuAddCliente:
                abrirAddCliente();
                break;

            case R.id.menuRegistro:
                abrirRegistro();
                break;

            case R.id.menuSobre:
                abrirSobre();
                break;

            case R.id.menuSair:
                deslogarUsuario();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirAddCliente() {
        startActivity(new Intent(EmpresaActivity.this, AddClienteActivity.class));
    }

    private void abrirRegistro() {
        startActivity(new Intent(EmpresaActivity.this, RegistroActivity.class));
    }

    private void abrirSobre() {
        startActivity(new Intent(EmpresaActivity.this, SobreActivity.class));
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarNotificacao() {

        //FirebaseMessaging.getInstance().subscribeToTopic("NotificacaoUsuario");
        String token = "et3Q8JE1t3o:APA91bFErYrHtWwx851KA4usLR1hKEVxLcM6CuxssDfCqHa-wlCCATUvwMy2TwszcIBT97zxSZ2GWVztqhDmee69HWqrpGi6ANvRBhnwoLstvkEuxRZot071W4IPZKDmpHBbNCh8mRMH";

        String to = "";
        to = token;

        Notificacao notificacao = new Notificacao("TôNaFila! - É a sua vez! :D", "Apresente o seu perfil na recepção.");
        NotificacaoDados notificacaoDados = new NotificacaoDados(to, notificacao);

        NotificacaoService service = retrofit.create(NotificacaoService.class);
        Call<NotificacaoDados> call = service.salvarNotificacao(notificacaoDados);

        call.enqueue(new Callback<NotificacaoDados>() {
            @Override
            public void onResponse(Call<NotificacaoDados> call, Response<NotificacaoDados> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            "Notificação enviada!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NotificacaoDados> call, Throwable t) {

            }
        });
    }

}