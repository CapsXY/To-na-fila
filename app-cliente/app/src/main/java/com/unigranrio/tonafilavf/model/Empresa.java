package com.unigranrio.tonafilavf.model;

import com.google.firebase.database.DatabaseReference;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;

import java.io.Serializable;

public class Empresa implements Serializable {

    private String idUsuario;
    private String urlImagemLogo;
    private String urlImagemBackground;
    private String nome;
    private String categoria;
    private String descricao;
    private String preco;
    private String horario;
    private String pagamento;
    private String informacao;
    private String contato;

    public Empresa() {

    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference empresaRef = firebaseRef
                .child("empresas")
                .child(getIdUsuario());
        empresaRef.setValue(this);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUrlImagemLogo() {
        return urlImagemLogo;
    }

    public void setUrlImagemLogo(String urlImagemLogo) {
        this.urlImagemLogo = urlImagemLogo;
    }

    public String getUrlImagemBackground() {
        return urlImagemBackground;
    }

    public void setUrlImagemBackground(String urlImagemBackground) {
        this.urlImagemBackground = urlImagemBackground;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}