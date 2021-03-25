package com.unigranrio.tonafilavfg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.unigranrio.tonafilavfg.R;
import com.unigranrio.tonafilavfg.helper.ConfiguracaoFirebase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("TôNaFila! - Administrador");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            
            case R.id.menuGerenciarFila:
                abrirGerenciarFila();
                break;
            case R.id.menuConfig:
                abrirConfiguracao();
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

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirGerenciarFila() {
        startActivity(new Intent(HomeActivity.this, FilaActivity.class));
    }

    private void abrirConfiguracao() {
        startActivity(new Intent(HomeActivity.this, ConfiguracaoActivity.class));
    }

    private void abrirSobre() {
        startActivity(new Intent(HomeActivity.this, SobreActivity.class));
    }
}