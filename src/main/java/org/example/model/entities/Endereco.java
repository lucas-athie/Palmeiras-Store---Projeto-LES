package org.example.model.entities;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Endereco {
    private int idEndereco;
    private String apelido;

    @SerializedName("tipoEndereco")
    private TEndereco tipoEndereco;

    private String cep;
    private String numero;
    private String bairro;
    private String observacoes;

    @SerializedName("tipoResidencia")
    private TResidencia tipoResidencia;

    @SerializedName("tipoLogradouro")
    private TLogradouro tipoLogradouro;

    private Logradouro logradouro;
    private Cidade cidade;
    private Estado estado;
    private Pais pais;

    public Endereco() {
    }

    public Endereco(
            int idEndereco,
            String apelido,
            TEndereco tipoEndereco,
            String cep,
            String numero,
            String bairro,
            String observacoes,
            TResidencia tipoResidencia,
            TLogradouro tipoLogradouro,
            Logradouro logradouro,
            Cidade cidade,
            Estado estado,
            Pais pais
    ) {
        this.idEndereco      = idEndereco;
        this.apelido         = apelido;
        this.tipoEndereco    = tipoEndereco;
        this.cep             = cep;
        this.numero          = numero;
        this.bairro          = bairro;
        this.observacoes     = observacoes;
        this.tipoResidencia  = tipoResidencia;
        this.tipoLogradouro  = tipoLogradouro;
        this.logradouro      = logradouro;
        this.cidade          = cidade;
        this.estado          = estado;
        this.pais            = pais;
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public TResidencia getTipoResidencia() {
        return tipoResidencia;
    }

    public void setTipoResidencia(TResidencia tipoResidencia) {
        this.tipoResidencia = tipoResidencia;
    }

    public TLogradouro getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(TLogradouro tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endereco)) return false;
        Endereco that = (Endereco) o;
        return idEndereco == that.idEndereco;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEndereco);
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "idEndereco=" + idEndereco +
                ", apelido='" + apelido + '\'' +
                ", tipoEndereco=" + tipoEndereco +
                ", cep='" + cep + '\'' +
                ", numero='" + numero + '\'' +
                ", bairro='" + bairro + '\'' +
                ", observacoes='" + observacoes + '\'' +
                ", tipoResidencia=" + tipoResidencia +
                ", tipoLogradouro=" + tipoLogradouro +
                ", logradouro=" + logradouro +
                ", cidade=" + cidade +
                ", estado=" + estado +
                ", pais=" + pais +
                '}';
    }
}