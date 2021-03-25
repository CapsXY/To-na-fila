package com.unigranrio.tonafilavf.activity.cliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.adapter.AdapterFila;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavf.helper.UsuarioFirebase;
import com.unigranrio.tonafilavf.listener.RecyclerItemClickListener;
import com.unigranrio.tonafilavf.model.DadosSolicitacao;
import com.unigranrio.tonafilavf.model.Empresa;
import com.unigranrio.tonafilavf.model.Fila;
import com.unigranrio.tonafilavf.model.Solicitacao;
import com.unigranrio.tonafilavf.model.Usuario;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class PaginaActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private TextView textViewNome;
    private TextView textViewCategoria;
    private TextView textViewDescricao;
    private TextView textViewPreco;
    private TextView textViewHorario;
    private TextView textViewPagamento;
    private TextView textViewInformacoes;
    private TextView textViewContato;
    private TextView textViewPessoasInseridas;
    private TextView textViewTotal;
    private RecyclerView recyclerView;
    private Empresa empresaSelecionada;
    private Usuario usuario;
    private Solicitacao solicitacaoRecuperada;
    private AlertDialog dialog;
    private AdapterFila adapterFila;
    private List<Fila> filas = new ArrayList<>();
    private List<DadosSolicitacao> dadosSolicitados = new ArrayList<>();
    private String idEmpresa;
    private String idUsuarioLogado;
    private int quantidadePessoas;
    private int categoriaPessoas;
    private Double totalSolicitado;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina);

        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            empresaSelecionada = (Empresa) bundle.getSerializable("empresa");

            idEmpresa = empresaSelecionada.getIdUsuario();

            String urlLogo = empresaSelecionada.getUrlImagemLogo();
            Picasso.get().load(urlLogo).into(imageViewLogo);

            textViewNome.setText(empresaSelecionada.getNome());
            textViewCategoria.setText(empresaSelecionada.getCategoria());
            textViewDescricao.setText(empresaSelecionada.getDescricao());
            textViewPreco.setText(empresaSelecionada.getPreco());
            textViewHorario.setText(empresaSelecionada.getHorario());
            textViewPagamento.setText(empresaSelecionada.getPagamento());
            textViewInformacoes.setText(empresaSelecionada.getInformacao());
            textViewContato.setText(empresaSelecionada.getContato());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterFila = new AdapterFila(filas, this);
        recyclerView.setAdapter(adapterFila);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                confirmarQuantidade(position);
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
        recuperarFilas();
        recuperarDadosUsuario();
    }

    private void confirmarQuantidade(final int posicao) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantidade");
        builder.setMessage("Informe quantas pessoas vão entrar na fila com você:");

        final EditText editQuantidade = new EditText(this);
        editQuantidade.setText("2");

        builder.setView(editQuantidade);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String quantidade = editQuantidade.getText().toString();

                Fila filaSelecionada = filas.get(posicao);
                DadosSolicitacao dadosSolicitacao = new DadosSolicitacao();
                dadosSolicitacao.setIdFila(filaSelecionada.getIdFila());
                dadosSolicitacao.setNomeFila(filaSelecionada.getNomeFila());
                dadosSolicitacao.setCode(filaSelecionada.getCodeFila());
                dadosSolicitacao.setQuantidade(Integer.parseInt(quantidade));

                dadosSolicitados.add(dadosSolicitacao);

                if (solicitacaoRecuperada == null) {
                    solicitacaoRecuperada = new Solicitacao(idUsuarioLogado, idEmpresa);
                }

                solicitacaoRecuperada.setNomeUsuario(usuario.getNome());
                solicitacaoRecuperada.setTelefoneUsuario(usuario.getTelefone());
                solicitacaoRecuperada.setItens(dadosSolicitados);
                solicitacaoRecuperada.setNomeEmpresa(empresaSelecionada.getNome());
                solicitacaoRecuperada.setEmail(usuario.getEmail());
                solicitacaoRecuperada.salvar();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void recuperarDadosUsuario() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados...")
                .setCancelable(false)
                .build();
        dialog.show();

        DatabaseReference usuariosRef = firebaseRef
                .child("usuarios")
                .child(idUsuarioLogado);

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
                recuperarSolicitacao();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void recuperarSolicitacao() {
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacao_usuario_temp")
                .child(idEmpresa)
                .child(idUsuarioLogado);

        solicitacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                quantidadePessoas = 0;
                totalSolicitado = 0.0;
                dadosSolicitados = new ArrayList<>();

                if (dataSnapshot.getValue() != null) {

                    solicitacaoRecuperada = dataSnapshot.getValue(Solicitacao.class);
                    dadosSolicitados = solicitacaoRecuperada.getDadosFila();

                    for (DadosSolicitacao dadosSolicitacao : dadosSolicitados) {

                        int quantidade = dadosSolicitacao.getQuantidade();
                        Double code = dadosSolicitacao.getCode();

                        totalSolicitado += (quantidade * code);
                        quantidadePessoas += quantidade;
                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");

                textViewPessoasInseridas.setText("" + String.valueOf(quantidadePessoas));
                textViewTotal.setText("" + df.format(totalSolicitado));
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void recuperarFilas() {

        DatabaseReference filasRef = firebaseRef
                .child("filas")
                .child(idEmpresa);

        filasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                filas.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    filas.add(ds.getValue(Fila.class));
                }
                adapterFila.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_solicitacao, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSolicitacao:
                confirmarSolicitacao();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmarSolicitacao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Possui algum:");

        CharSequence[] itens = new CharSequence[]{
                "PNE / Fumante", "Nenhum"
        };
        builder.setSingleChoiceItems(itens, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoriaPessoas = which;
            }
        });

        final EditText editObservacao = new EditText(this);
        editObservacao.setHint("Digite uma observação");
        builder.setView(editObservacao);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String observacao = editObservacao.getText().toString();
                solicitacaoRecuperada.setOpcao(categoriaPessoas);
                solicitacaoRecuperada.setObservacao(observacao);
                solicitacaoRecuperada.setStatus("Aguardando");
                solicitacaoRecuperada.confirmar();
                solicitacaoRecuperada.confirmarUsuario();
                solicitacaoRecuperada.confirmarUsuarioHistorico();
                solicitacaoRecuperada.remover();
                solicitacaoRecuperada = null;

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void inicializarComponentes() {
        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewNome = findViewById(R.id.textViewNomePagina);
        textViewCategoria = findViewById(R.id.textViewCategoriaPagina);
        textViewDescricao = findViewById(R.id.textViewSobre);
        textViewPreco = findViewById(R.id.textViewPreco);
        textViewHorario = findViewById(R.id.textViewHorario);
        textViewInformacoes = findViewById(R.id.textViewInformacoes);
        textViewPagamento = findViewById(R.id.textViewPagamento);
        textViewContato = findViewById(R.id.textViewContato);
        recyclerView = findViewById(R.id.recyclerViewPagina);
        textViewPessoasInseridas = findViewById(R.id.textViewPessoasInseridas);
        textViewTotal = findViewById(R.id.textViewTotal);
    }
}