package com.unigranrio.tonafilavf.activity.comum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.helper.UsuarioFirebase;

public class TelaInicialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

    }

    public void abrirTelaConectar(View view) {
        startActivity(new Intent(this, ConectarActivity.class));
    }

    public void abrirTelaCadastrar(View view) {
        startActivity(new Intent(this, CadastrarActivity.class));
    }

    //MANTER USUARIO CONECTADO
    @Override
    protected void onStart() {
        super.onStart();
        UsuarioFirebase.redirecionaUsuarioLogado(TelaInicialActivity.this);
    }
}
