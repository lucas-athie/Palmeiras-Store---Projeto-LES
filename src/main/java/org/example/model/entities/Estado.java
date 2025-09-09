package org.example.model.entities;

import java.util.Objects;

public class Estado {
    private String nome;
    private String sigla;

    public Estado() {
    }

    public Estado(String sigla) {
        this.sigla = sigla;
    }


    public Estado(String nome, String sigla) {
        this.nome  = nome;
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public static Estado from(String sigla) {
        if (sigla == null) {
            return null;
        }
        return new Estado(sigla);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estado)) return false;
        Estado estado = (Estado) o;
        return Objects.equals(nome, estado.nome) &&
                Objects.equals(sigla, estado.sigla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, sigla);
    }

    @Override
    public String toString() {
        return nome + " (" + sigla + ")";
    }
}