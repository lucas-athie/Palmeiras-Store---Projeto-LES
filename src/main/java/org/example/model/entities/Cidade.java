package org.example.model.entities;

public class Cidade {
    private String nome;

    public Cidade() {
    }

    public Cidade(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static Cidade from(String nome) {
        if (nome == null) {
            return null;
        }
        return new Cidade(nome);
    }

    @Override
    public String toString() {
        return nome;
    }
}