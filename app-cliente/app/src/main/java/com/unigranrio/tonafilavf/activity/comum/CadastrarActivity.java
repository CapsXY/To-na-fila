package com.unigranrio.tonafilavf.activity.comum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.activity.cliente.UsuarioActivity;
import com.unigranrio.tonafilavf.activity.empresa.EmpresaActivity;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavf.helper.UsuarioFirebase;
import com.unigranrio.tonafilavf.model.Usuario;

public class CadastrarActivity extends AppCompatActivity {

    private TextView campoNome, campoEmail, campoTelefone, campoSenha;
    private Switch switchTipoUsuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        campoNome = findViewById(R.id.editCadastroNome);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoTelefone = findViewById(R.id.editCadastroTelefone);
        campoSenha = findViewById(R.id.editCadastroSenha);
        switchTipoUsuario = findViewById(R.id.switchTipoUsuario);

    }

    public void validarCadastroUsuario(View view) {

        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoTelefone = campoTelefone.getText().toString();
        String textoSenha = campoSenha.getText().toString();


        if (!textoNome.isEmpty()) {
            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {

                    Usuario usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setTelefone(textoTelefone);
                    usuario.setSenha(textoSenha);
                    usuario.setTipo(verificaTipoUsuario());

                    cadastrarUsuario(usuario);

                } else {
                    Toast.makeText(CadastrarActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CadastrarActivity.this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CadastrarActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastrarUsuario(final Usuario usuario) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    try {

                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setIdUsuario(idUsuario);
                        usuario.salvar();

                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                        if (verificaTipoUsuario() == "usuario") {
                            startActivity(new Intent(CadastrarActivity.this, UsuarioActivity.class));
                            finish();
                            Toast.makeText(CadastrarActivity.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(CadastrarActivity.this, EmpresaActivity.class));
                            finish();
                            Toast.makeText(CadastrarActivity.this, "Sucesso ao cadastrar empresa!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um e-mail válido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Esta conta já foi cadastrada!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário:" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastrarActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String verificaTipoUsuario() {
        return switchTipoUsuario.isChecked() ? "empresa" : "usuario";
    }

    public void abrirTermos(View view) {
        startActivity(new Intent(CadastrarActivity.this, TermosActivity.class));
    }
}