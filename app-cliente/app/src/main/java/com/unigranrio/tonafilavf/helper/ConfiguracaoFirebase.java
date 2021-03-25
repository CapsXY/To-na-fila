package com.unigranrio.tonafilavf.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    public static DatabaseReference referenciaDatabase;
    public static FirebaseAuth referenciaAutenticacao;
    private static StorageReference referenciaStorage;

    public static DatabaseReference getFirebaseDatabase() {
        if (referenciaDatabase == null) {
            referenciaDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaDatabase;
    }

    public static FirebaseAuth getFirebaseAutenticacao() {
        if (referenciaAutenticacao == null) {
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }

    public static StorageReference getReferenciaStorage() {
        if (referenciaStorage == null) {
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }
}
