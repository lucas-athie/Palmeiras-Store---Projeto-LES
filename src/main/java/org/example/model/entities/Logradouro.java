package org.example.model.entities;

import java.util.Objects;

public class Logradouro {
    private String nome;

    public Logradouro() {
    }

    public Logradouro(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static Logradouro from(String nome) {
        if (nome == null) {
            return null;
        }
        return new Logradouro(nome);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Logradouro)) return false;
        Logradouro that = (Logradouro) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return nome;
    }
}