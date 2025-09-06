package org.example.model.entities;

import java.util.Date;
import java.util.List;

public class Cliente {
    private String nome;
    private Date dataNascimento;
    private String cpf;
    private String email;
    private Telefone telefone;
    private Genero genero;
    private List<Endereco> endereco;
    private List<Cartao> cartao;
    public Cliente() {
    }
    public Cliente(String nome, Date dataNascimento, String cpf, String email, Telefone telefone, Genero genero) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.genero = genero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }
}
