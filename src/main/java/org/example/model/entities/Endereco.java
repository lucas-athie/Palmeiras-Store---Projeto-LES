package org.example.model.entities;

public class Endereco {
    private int idEndereco;
    private String apelido;
    private TEndereco tipoEndereco;
    private String cep;
    private String numero;
    private String bairro;
    private TResidencia residencia;
    private TLogradouro tLogradouro;
    private Logradouro logradouro;
    private Cidade cidade;
    private Pais pais;
    private Estado estado;
    private String observacoes;

    public Endereco(int idEndereco, String apelido, TEndereco tipoEndereco, String cep, String numero, String bairro, String observacoes, TResidencia residencia, TLogradouro tLogradouro, Logradouro logradouro, Cidade cidade, Pais pais, Estado estado) {
        this.idEndereco = idEndereco;
        this.apelido = apelido;
        this.tipoEndereco = tipoEndereco;
        this.cep = cep;
        this.numero = numero;
        this.bairro = bairro;
        this.observacoes = observacoes;
        this.residencia = residencia;
        this.tLogradouro = tLogradouro;
        this.logradouro = logradouro;
        this.cidade = cidade;
        this.pais = pais;
        this.estado = estado;
    }

    public Endereco() {
    }

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public TEndereco getTipoEndereco() {
        return tipoEndereco;
    }
    public void setTipoEndereco(TEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public TResidencia getResidencia() {
        return residencia;
    }

    public void setResidencia(TResidencia residencia) {
        this.residencia = residencia;
    }

    public TLogradouro gettLogradouro() {
        return tLogradouro;
    }

    public void settLogradouro(TLogradouro tLogradouro) {
        this.tLogradouro = tLogradouro;
    }

    public Logradouro getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
