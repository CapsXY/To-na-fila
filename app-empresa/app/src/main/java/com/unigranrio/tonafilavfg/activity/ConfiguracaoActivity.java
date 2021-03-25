package com.unigranrio.tonafilavfg.activity;

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
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
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
import com.unigranrio.tonafilavfg.R;
import com.unigranrio.tonafilavfg.helper.ConfiguracaoFirebase;
import com.unigranrio.tonafilavfg.helper.UsuarioFirebase;
import com.unigranrio.tonafilavfg.model.Empresa;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class ConfiguracaoActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private EditText editTextNome;
    private EditText editTextCategoria;
    private EditText editTextDescricao;
    private EditText editTextPreco;
    private EditText editTextHorario;
    private EditText editTextPagamento;
    private EditText editTextInformacoes;
    private EditText editTextContato;
    private String idUsuarioLogado;
    private String urlImagemSelecionadaLogo = "";
    private DatabaseReference firebaseRef;
    private StorageReference storageReference;
    private static final int selecaoGaleria = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        inicializarComponentes();
        storageReference = ConfiguracaoFirebase.getReferenciaStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        imageViewLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, selecaoGaleria);
                }
            }
        });

        recuperarDadosEmpresa();
    }

    private void recuperarDadosEmpresa() {

        DatabaseReference empresaRef = firebaseRef
                .child("empresas")
                .child(idUsuarioLogado);

        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    Empresa empresa = dataSnapshot.getValue(Empresa.class);
                    editTextNome.setText(empresa.getNome());
                    editTextCategoria.setText(empresa.getCategoria());
                    editTextPreco.setText(empresa.getPreco());
                    editTextHorario.setText(empresa.getHorario());
                    editTextDescricao.setText(empresa.getDescricao());
                    editTextContato.setText(empresa.getContato());
                    editTextInformacoes.setText(empresa.getInformacao());
                    editTextPagamento.setText(empresa.getPagamento());
                    urlImagemSelecionadaLogo = empresa.getUrlImagemLogo();

                    if (urlImagemSelecionadaLogo != "") {
                        Picasso.get().load(urlImagemSelecionadaLogo).into(imageViewLogo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validarDadosEmpresa(View view) {

        String editTextNome = this.editTextNome.getText().toString();
        String editTextCategoria = this.editTextCategoria.getText().toString();
        String editTextPreco = this.editTextPreco.getText().toString();
        String editTextHorario = this.editTextHorario.getText().toString();
        String editTextDescricao = this.editTextDescricao.getText().toString();
        String editTextContato = this.editTextContato.getText().toString();
        String editTextInformacoes = this.editTextInformacoes.getText().toString();
        String editTextPagamento = this.editTextPagamento.getText().toString();

        if (!editTextNome.isEmpty()) {
            if (!editTextCategoria.isEmpty()) {
                if (!editTextPreco.isEmpty()) {
                    if (!editTextHorario.isEmpty()) {
                        if (!editTextDescricao.isEmpty()) {
                            if (!editTextContato.isEmpty()) {
                                if (!editTextInformacoes.isEmpty()) {
                                    if(!editTextPagamento.isEmpty()){

                                    Empresa empresa = new Empresa();
                                    empresa.setIdUsuario(idUsuarioLogado);
                                    empresa.setNome(editTextNome);
                                    empresa.setCategoria(editTextCategoria);
                                    empresa.setPreco(editTextPreco);
                                    empresa.setHorario(editTextHorario);
                                    empresa.setDescricao(editTextDescricao);
                                    empresa.setContato(editTextContato);
                                    empresa.setInformacao(editTextInformacoes);
                                    empresa.setPagamento(editTextPagamento);
                                    empresa.setUrlImagemLogo(urlImagemSelecionadaLogo);
                                    empresa.salvar();

                                    exibirMensagem("Dados atualizados com sucesso!");
                                    finish();

                                    } else {
                                        exibirMensagem("Digite uma forma de pagamento");
                                    }
                                } else {
                                    exibirMensagem("Digite alguma informação adicional. Ex.: Possui acesso para deficientes");
                                }
                            } else {
                                exibirMensagem("Digite um telefone para contato");
                            }
                        } else {
                            exibirMensagem("Digite alguma descrição sobre o restaurante");
                        }
                    } else {
                        exibirMensagem("Digite os horários de funcionamento");
                    }
                } else {
                    exibirMensagem("Digite um preço médio de consumo por pessoa");
                }
            } else {
                exibirMensagem("Digite uma categoria para sua empresa");
            }
        } else {
            exibirMensagem("Digite um nome para sua empresa");
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
                    imageViewLogo.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("empresas")
                            .child("logos")
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
                                urlImagemSelecionadaLogo = downloadUrl.toString();
                                Toast.makeText(ConfiguracaoActivity.this, "Sucesso ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ConfiguracaoActivity.this, "Erro ao fazer upload da imagem.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void inicializarComponentes() {

        imageViewLogo = findViewById(R.id.imageViewLogo);
        editTextNome = findViewById(R.id.editTextNome);
        editTextCategoria = findViewById(R.id.editTextCategoria);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextPreco = findViewById(R.id.editTextPreco);
        editTextHorario = findViewById(R.id.editTextHorario);
        editTextPagamento = findViewById(R.id.editTextPagamento);
        editTextInformacoes = findViewById(R.id.editTextInformacoes);
        editTextContato = findViewById(R.id.editTextContato);
    }
}