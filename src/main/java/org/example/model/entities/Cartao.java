package org.example.model.entities;

public class Cartao {
    private int id;
    private boolean principal;
    private String numero;
    private String titular;
    private Bandeira bandeira;
    private String codigoSeguranca;

    public Cartao() {
    }

    public Cartao(int id,
                  boolean principal,
                  String numero,
                  String titular,
                  Bandeira bandeira,
                  String codigoSeguranca) {
        this.id = id;
        this.principal = principal;
        this.numero = numero;
        this.titular = titular;
        this.bandeira = bandeira;
        this.codigoSeguranca = codigoSeguranca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
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

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }

    public void setCodigoSeguranca(String codigoSeguranca) {
        this.codigoSeguranca = codigoSeguranca;
    }
}