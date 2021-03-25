package com.unigranrio.tonafilavfg.model;

import com.google.firebase.database.DatabaseReference;
import com.unigranrio.tonafilavfg.helper.ConfiguracaoFirebase;

public class Fila {

    private String idUsuario;
    private String idFila;
    private String responsavelFila;
    private Double codeFila;
    private String dataFila;

    public Fila() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference filaRef = firebaseRef
                .child("filas");
        setIdFila(filaRef.push().getKey());
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference filaRef = firebaseRef
                .child("filas")
                .child(getIdUsuario())
                .child(getIdFila());
        filaRef.setValue(this);
    }

    public void remover() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference filaRef = firebaseRef
                .child("filas")
                .child(getIdUsuario());
        filaRef.removeValue();
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdFila() {
        return idFila;
    }

    public void setIdFila(String idFila) {
        this.idFila = idFila;
    }

    public String getResponsavelFila() {
        return responsavelFila;
    }

    public void setResponsavelFila(String responsavelFila) {
        this.responsavelFila = responsavelFila;
    }

    public Double getCodeFila() {
        return codeFila;
    }

    public void setCodeFila(Double codeFila) {
        this.codeFila = codeFila;
    }

    public String getDataFila() {
        return dataFila;
    }

    public void setDataFila(String dataFila) {
        this.dataFila = dataFila;
    }
}
