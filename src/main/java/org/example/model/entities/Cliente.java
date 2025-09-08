package org.example.model.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cliente {
    private int idCliente;
    private String codigo;
    private Boolean ativo;
    private String nome;
    private Date dataNascimento;
    private String cpf;
    private String email;
    private String rank;
    private Telefone telefone;
    private Genero genero;
    private List<Endereco> endereco = new ArrayList<>();
    private List<Cartao> cartao = new ArrayList<>();
    private String senha;
    public Cliente() {
    }
    public Cliente(Integer idCliente, String codigo, Boolean ativo, String nome, Date dataNascimento, String cpf, String email, String rank, Telefone telefone, Genero genero, String senha) {
        this.idCliente = idCliente;
        this.codigo = codigo;
        this.ativo = ativo;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.email = email;
        this.rank = rank;
        this.telefone = telefone;
        this.genero = genero;
        this.senha = senha;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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

    public List<Endereco> getEndereco() {
        return endereco;
    }

    public void setEndereco(List<Endereco> endereco) {
        this.endereco = endereco;
    }

    public List<Cartao> getCartao() {
        return cartao;
    }

    public void setCartao(List<Cartao> cartao) {
        this.cartao = cartao;
    }

    public void addEndereco(Endereco endereco) {
        this.endereco.add(endereco);
    }

    public void addCartao(Cartao cartao) {
        this.cartao.add(cartao);
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}



