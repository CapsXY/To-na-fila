package com.unigranrio.tonafilavf.model;

import com.google.firebase.database.DatabaseReference;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;

public class Fila {

    private String idUsuario;
    private String idFila;
    private String nomeFila;
    private Double codeFila;

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
                .child(getIdUsuario())
                .child(getIdFila());
        filaRef.setValue(this);
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

    public String getNomeFila() {
        return nomeFila;
    }

    public void setNomeFila(String nomeFila) {
        this.nomeFila = nomeFila;
    }

    public Double getCodeFila() {
        return codeFila;
    }

    public void setCodeFila(Double codeFila) {
        this.codeFila = codeFila;
    }
}
