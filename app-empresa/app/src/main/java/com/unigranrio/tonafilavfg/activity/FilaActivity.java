package com.unigranrio.tonafilavfg.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.unigranrio.tonafilavfg.R;
import com.unigranrio.tonafilavfg.helper.UsuarioFirebase;
import com.unigranrio.tonafilavfg.model.Fila;

public class FilaActivity extends AppCompatActivity {

    private EditText editTextResponsavelFila;
    private EditText editTextCodeFila;
    private EditText editTextDataFila;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fila);

        inicializarComponentes();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
    }

    public void IniciarFila(View view) {

        String nomeResponsavel = editTextResponsavelFila.getText().toString();
        String codeFila = editTextCodeFila.getText().toString();
        String dataFila = editTextDataFila.getText().toString();

        if (!nomeResponsavel.isEmpty()) {
            if (!codeFila.isEmpty()) {
                if (!dataFila.isEmpty()) {

                    Fila fila = new Fila();
                    fila.setIdUsuario(idUsuarioLogado);
                    fila.setResponsavelFila(nomeResponsavel);
                    fila.setCodeFila(Double.parseDouble(codeFila));
                    fila.setDataFila(dataFila);
                    fila.salvar();
                    finish();
                    exibirMensagem("Fila iniciada com sucesso!");
                } else {
                    exibirMensagem("");
                }
            } else {
                exibirMensagem("");
            }
        } else {
            exibirMensagem("");
        }
    }

    public void FinalizarFila(View view) {

        String nomeResponsavel = editTextResponsavelFila.getText().toString();
        String codeFila = editTextCodeFila.getText().toString();
        String dataFila = editTextDataFila.getText().toString();

        if (!nomeResponsavel.isEmpty()) {
            if (!codeFila.isEmpty()) {
                if (!dataFila.isEmpty()) {

                    Fila fila = new Fila();
                    fila.setIdUsuario(idUsuarioLogado);
                    fila.setResponsavelFila(nomeResponsavel);
                    fila.setCodeFila(Double.parseDouble(codeFila));
                    fila.setDataFila(dataFila);
                    fila.remover();
                    finish();
                    exibirMensagem("Fila encerrada com sucesso!");
                } else {
                    exibirMensagem("");
                }
            } else {
                exibirMensagem("");
            }
        } else {
            exibirMensagem("");
        }
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private void inicializarComponentes() {
        editTextResponsavelFila = findViewById(R.id.editTextResponsavelFila);
        editTextCodeFila = findViewById(R.id.editTextCodeFila);
        editTextDataFila = findViewById(R.id.editTextDataFila);
    }
}