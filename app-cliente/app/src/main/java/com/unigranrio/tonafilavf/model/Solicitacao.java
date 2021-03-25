package com.unigranrio.tonafilavf.model;

import com.google.firebase.database.DatabaseReference;
import com.unigranrio.tonafilavf.helper.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.List;

public class Solicitacao {

    private String idUsuario;
    private String idEmpresa;
    private String idSolicitacao;
    private String nomeUsuario;
    private String nomeEmpresa;
    private String telefoneUsuario;
    private String observacao;
    private String email;
    private String status = "Pendente";
    private List<DadosSolicitacao> dadosFila;
    private Double total;
    private int opcao;

    public Solicitacao() {
    }

    //PAGINA ACTIVITY///////////////////////////////////////////////////////////////////////////////
    public Solicitacao(String idUsu, String idEmp) {

        setIdUsuario(idUsu);
        setIdEmpresa(idEmp);

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacao_usuario_temp")
                .child(idEmp)
                .child(idUsu);
        setIdSolicitacao(solicitacaoRef.push().getKey());
    }


    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacao_usuario_temp")
                .child(getIdEmpresa())
                .child(getIdUsuario());
        solicitacaoRef.setValue(this);
    }

    public void remover() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacao_usuario_temp")
                .child(getIdEmpresa())
                .child(getIdUsuario());
        solicitacaoRef.removeValue();
    }

    //EMPRESA ACTIVITY//////////////////////////////////////////////////////////////////////////////
    public void confirmar() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes")
                .child(getIdEmpresa())
                .child(getIdSolicitacao());
        solicitacaoRef.setValue(this);
    }

    public void atualizarStatus() {

        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes")
                .child(getIdEmpresa())
                .child(getIdSolicitacao());
        solicitacaoRef.updateChildren(status);

    }

    public void removerSolicitacao() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes")
                .child(getIdEmpresa())
                .child(getIdSolicitacao());
        solicitacaoRef.removeValue();
    }

    //FILA USUARIO ACTIVITY/////////////////////////////////////////////////////////////////////////
    public void confirmarUsuario() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes_usuario")
                .child(getIdUsuario())
                .child(getIdSolicitacao());
        solicitacaoRef.setValue(this);
    }

    public void atualizarStatusUsuario() {

        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes_usuario")
                .child(getIdUsuario())
                .child(getIdSolicitacao());
        solicitacaoRef.updateChildren(status);

    }

    public void removerSolicitacaoUsuario() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes_usuario")
                .child(getIdUsuario())
                .child(getIdSolicitacao());
        solicitacaoRef.removeValue();
    }

    //HISTORICO ACTIVITY////////////////////////////////////////////////////////////////////////////
    public void confirmarUsuarioHistorico() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes_usuario_historico")
                .child(getIdUsuario())
                .child(getIdSolicitacao());
        solicitacaoRef.setValue(this);
    }

    public void atualizarStatusUsuarioHistorico() {

        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes_usuario_historico")
                .child(getIdUsuario())
                .child(getIdSolicitacao());
        solicitacaoRef.updateChildren(status);

    }

    public void removerSolicitacaoUsuarioHistorico() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference solicitacaoRef = firebaseRef
                .child("solicitacoes_usuario_historico")
                .child(getIdUsuario())
                .child(getIdSolicitacao());
        solicitacaoRef.removeValue();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(String idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getTelefoneUsuario() {
        return telefoneUsuario;
    }

    public void setTelefoneUsuario(String telefoneUsuario) {
        this.telefoneUsuario = telefoneUsuario;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DadosSolicitacao> getDadosFila() {
        return dadosFila;
    }

    public void setItens(List<DadosSolicitacao> dadosFila) {
        this.dadosFila = dadosFila;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
