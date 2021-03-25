package com.unigranrio.tonafilavfg.model;

import com.google.firebase.database.DatabaseReference;
import com.unigranrio.tonafilavfg.helper.ConfiguracaoFirebase;

public class Usuario {

    private String idUsuario;
    private String urlImagemUsuario;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String tipo;

    public Usuario() {

    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef
                .child("usuarios")
                .child(getIdUsuario());

        usuarios.setValue(this);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUrlImagemUsuario() {
        return urlImagemUsuario;
    }

    public void setUrlImagemUsuario(String urlImagemUsuario) {
        this.urlImagemUsuario = urlImagemUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
