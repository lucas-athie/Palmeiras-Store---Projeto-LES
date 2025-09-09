package org.example.model.entities;

import java.util.Objects;

public class Telefone {
    private String numero;
    private String ddd;
    private TTelefone tipo;

    public Telefone() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Telefone)) return false;
        Telefone that = (Telefone) o;
        return Objects.equals(ddd, that.ddd) &&
                Objects.equals(numero, that.numero) &&
                tipo == that.tipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ddd, numero, tipo);
    }

    @Override
    public String toString() {
        return "(" + ddd + ") " + numero + " [" + tipo + "]";
    }
}