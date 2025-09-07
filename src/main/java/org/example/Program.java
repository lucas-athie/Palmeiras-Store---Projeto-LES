package org.example;

import org.example.db.DB;
import org.example.model.dao.ClienteDao;
import org.example.model.dao.DaoFactory;
import org.example.model.entities.Cliente;
import org.example.model.entities.Cartao;
import org.example.model.entities.Endereco;

import java.sql.Connection;

public class Program {
    public static void main(String[] args) {
        try (Connection conn = DB.getConnection()) {

            // cria o DAO passando a conexão
            ClienteDao clienteDao = DaoFactory.createClienteDao(conn);

            // ID fixo para testar
            int idTeste = 3; // coloque um ID que exista no seu banco
            Cliente cliente = clienteDao.findById(idTeste);

            if (cliente != null) {
                System.out.println("=== CLIENTE ===");
                System.out.println("ID: " + cliente.getIdCliente());
                System.out.println("Código: " + cliente.getCodigo());
                System.out.println("Nome: " + cliente.getNome());
                System.out.println("Email: " + cliente.getEmail());
                System.out.println("Ativo: " + cliente.getAtivo());
                System.out.println("Data Nasc.: " + cliente.getDataNascimento());
                System.out.println("CPF: " + cliente.getCpf());
                System.out.println("Rank: " + cliente.getRank());
                System.out.println("Gênero: " + cliente.getGenero());
                System.out.println("Telefone: " + cliente.getTelefone());

                System.out.println("\n=== CARTÕES ===");
                for (Cartao c : cliente.getCartao()) {
                    System.out.println("ID: " + c.getId() +
                            ", Número: " + c.getNumero() +
                            ", Titular: " + c.getTitular() +
                            ", Bandeira: " + c.getBandeira() +
                            ", Principal: " + c.getPrincipal());
                }

                System.out.println("\n=== ENDEREÇOS ===");
                for (Endereco e : cliente.getEndereco()) {
                    System.out.println("Apelido: " + e.getApelido() +
                            ", Número: " + e.getNumero() +
                            ", CEP: " + e.getCep() +
                            ", Cidade: " + e.getCidade().getNome() +
                            ", Estado: " + e.getEstado().getNome() +
                            " (" + e.getEstado().getSigla() + ")" +
                            ", País: " + e.getPais().getNome() +
                            ", Tipo Residência: " + e.getResidencia() +
                            ", Tipo Logradouro: " + e.gettLogradouro());
                }
            } else {
                System.out.println("Nenhum cliente encontrado para o ID " + idTeste);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}