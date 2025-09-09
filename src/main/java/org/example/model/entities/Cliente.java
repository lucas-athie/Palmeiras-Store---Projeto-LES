package org.example.model.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Cliente {
    private int idCliente;
    private String codigo;
    private boolean ativo;
    private String nome;
    private Date dataNascimento;
    private String cpf;
    private String email;
    private Telefone telefone;
    private Genero genero;
    private List<Endereco> enderecos = new ArrayList<>();
    private List<Cartao> cartoes    = new ArrayList<>();
    private String senha;

    public Cliente() {
    }

    public Cliente(int idCliente,
                   String codigo,
                   boolean ativo,
                   String nome,
                   Date dataNascimento,
                   String cpf,
                   String email,
                   Telefone telefone,
                   Genero genero,
                   String senha) {
        this.idCliente      = idCliente;
        this.codigo         = codigo;
        this.ativo          = ativo;
        this.nome           = nome;
        this.dataNascimento = dataNascimento;
        this.cpf            = cpf;
        this.email          = email;
        this.telefone       = telefone;
        this.genero         = genero;
        this.senha          = senha;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
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

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void addEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
    }

    public void addCartao(Cartao cartao) {
        this.cartoes.add(cartao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente that = (Cliente) o;
        return idCliente == that.idCliente;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", codigo='" + codigo + '\'' +
                ", ativo=" + ativo +
                ", nome='" + nome + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", telefone=" + telefone +
                ", genero=" + genero +
                ", enderecos=" + enderecos +
                ", cartoes=" + cartoes +
                '}';
    }
}