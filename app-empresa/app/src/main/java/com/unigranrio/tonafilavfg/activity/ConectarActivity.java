package com.unigranrio.tonafilavfg.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.unigranrio.tonafilavfg.R;
import com.unigranrio.tonafilavfg.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavfg.helper.UsuarioFirebase;
import com.unigranrio.tonafilavfg.model.Usuario;

public class ConectarActivity extends AppCompatActivity {

    private TextView campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conectar);

        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
    }

    public void validarLoginUsuario(View view) {

        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if (!textoEmail.isEmpty()) {
            if (!textoSenha.isEmpty()) {

                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);

                logarUsuario(usuario);

            } else {
                Toast.makeText(ConectarActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ConectarActivity.this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
        }

    }

    public void logarUsuario(Usuario usuario) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    UsuarioFirebase.redirecionaUsuarioLogado(ConectarActivity.this);

                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não está cadastrado!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário!";
                        e.printStackTrace();
                    }
                    Toast.makeText(ConectarActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        UsuarioFirebase.redirecionaUsuarioLogado(ConectarActivity.this);
    }
}