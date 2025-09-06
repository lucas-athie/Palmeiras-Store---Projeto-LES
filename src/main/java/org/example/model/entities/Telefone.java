package org.example.model.entities;

public class Telefone {
    private String numero;
    private TTelefone tipo;

    public Telefone(String numero, TTelefone tipo) {
        this.numero = numero;
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TTelefone getTipo() {
        return tipo;
    }

    public void setTipo(TTelefone tipo) {
        this.tipo = tipo;
    }
}
