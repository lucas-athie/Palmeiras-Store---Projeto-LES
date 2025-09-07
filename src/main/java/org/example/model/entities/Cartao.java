package org.example.model.entities;

public class Cartao {
    private int idCartao;
    private Boolean principal;
    private String numero;
    private String titular;
    private Bandeira bandeira;
    private String codigoSeguranca;

    public Cartao(Integer idCartao, Boolean principal, String numero, String titular, Bandeira bandeira, String codigoSeguranca) {
        this.idCartao = idCartao;
        this.principal = principal;
        this.numero = numero;
        this.titular = titular;
        this.bandeira = bandeira;
        this.codigoSeguranca = codigoSeguranca;
    }

    public int getId() {
        return idCartao;
    }

    public void setId(int id) {
        this.idCartao = id;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Cartao() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public Bandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeira bandeira) {
        this.bandeira = bandeira;
    }

    public void setCodigoSeguranca(String codigoSeguranca) {
        this.codigoSeguranca = codigoSeguranca;
    }
}
