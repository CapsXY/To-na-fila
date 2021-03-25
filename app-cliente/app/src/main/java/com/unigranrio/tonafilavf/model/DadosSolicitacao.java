package com.unigranrio.tonafilavf.model;

public class DadosSolicitacao {

    private String idFila;
    private String nomeFila;
    private int quantidade;
    private Double code;

    public DadosSolicitacao(){

    }

    public String getIdFila() {
        return idFila;
    }

    public void setIdFila(String idFila) {
        this.idFila = idFila;
    }

    public String getNomeFila() {
        return nomeFila;
    }

    public void setNomeFila(String nomeFila) {
        this.nomeFila = nomeFila;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getCode() {
        return code;
    }

    public void setCode(Double code) {
        this.code = code;
    }
}
