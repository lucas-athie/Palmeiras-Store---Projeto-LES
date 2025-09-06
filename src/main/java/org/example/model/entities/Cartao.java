package org.example.model.entities;

public class Cartao {
    private String numero;
    private String titular;
    private Bandeira bandeira;
    private String codigoSeguranca;

    public Cartao(String numero, String titular, Bandeira bandeira, String codigoSeguranca) {
        this.numero = numero;
        this.titular = titular;
        this.bandeira = bandeira;
        this.codigoSeguranca = codigoSeguranca;
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
