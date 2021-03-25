package com.unigranrio.tonafilavf.activity.cliente;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.unigranrio.tonafilavf.R;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavf.helper.UsuarioFirebase;
import com.unigranrio.tonafilavf.model.Usuario;

import java.io.ByteArrayOutputStream;

public class PerfilActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private EditText campoNome;
    private EditText campoTelefone;
    private EditText campoTipo;
    private TextView textEmail;
    private ImageView imagePerfilUsuario;
    private StorageReference storageReference;
    private String idUsuarioLogado, urlImagemSelecionada = "";
    private DatabaseReference firebaseRef;
    private static final int selecaoGaleria = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        inicializarComponentes();
        storageReference = ConfiguracaoFirebase.getReferenciaStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        imagePerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, selecaoGaleria);
                }
            }
        });

        recuperarDadosUsuario();
    }

    private void recuperarDadosUsuario() {

        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(idUsuarioLogado);

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    campoNome.setText(usuario.getNome());
                    textEmail.setText(usuario.getEmail());
                    campoTelefone.setText(usuario.getTelefone());
                    urlImagemSelecionada = usuario.getUrlImagemUsuario();

                    if (urlImagemSelecionada != "") {
                        Picasso.get().load(urlImagemSelecionada).into(imagePerfilUsuario);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validarDadosUsuario(View view) {

        String nome = campoNome.getText().toString();
        String email = textEmail.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String tipo = campoTipo.getText().toString();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!telefone.isEmpty()) {

                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(idUsuarioLogado);
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setTelefone(telefone);
                    usuario.setUrlImagemUsuario(urlImagemSelecionada);
                    usuario.setTipo(tipo);
                    usuario.salvar();

                    exibirMensagem("Dados atualizados com sucesso!");
                    finish();

                } else {
                    exibirMensagem("Digite seu telefone");
                }
            } else {
                exibirMensagem("Digite seu e-mail");
            }

        } else {
            exibirMensagem("Digite seu nome");
        }
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {
                switch (requestCode) {
                    case selecaoGaleria:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagem);
                        break;
                }
                if (imagem != null) {
                    imagePerfilUsuario.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("usuarios")
                            .child(idUsuarioLogado + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imagemRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlImagemSelecionada = downloadUrl.toString();
                                Toast.makeText(PerfilActivity.this, "Sucesso ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PerfilActivity.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void inicializarComponentes() {
        campoNome = findViewById(R.id.editNomeUsuario);
        textEmail = findViewById(R.id.textViewEmailUsuario);
        campoTelefone = findViewById(R.id.editTelefoneUsuario);
        imagePerfilUsuario = findViewById(R.id.imageViewLogo);
        campoTipo = findViewById(R.id.editTextTipoUsuario);
    }
}