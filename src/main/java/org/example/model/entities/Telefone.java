package org.example.model.entities;

public class Telefone {
    private String numero;
    private String ddd;
    private TTelefone tipo;

    public Telefone(String numero, String ddd, TTelefone tipo) {
        this.numero = numero;
        this.ddd = ddd;
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public TTelefone getTipo() {
        return tipo;
    }

    public void setTipo(TTelefone tipo) {
        this.tipo = tipo;
    }
}
