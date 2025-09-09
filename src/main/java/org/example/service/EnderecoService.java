package org.example.service;

import org.example.db.DB;
import org.example.model.dao.EnderecoDao;
import org.example.model.dao.impl.EnderecoDaoJDBC;
import org.example.model.entities.Endereco;
import org.example.service.strategy.EnderecoValidator;
import org.example.service.strategy.ValidationResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class EnderecoService {

    private final EnderecoValidator validator = new EnderecoValidator();

    public void criar(Endereco endereco, int clienteId) {
        ValidationResult vr = validator.validate(endereco);
        vr.assertValid();

        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                EnderecoDao dao = new EnderecoDaoJDBC(conn);
                dao.insert(clienteId, endereco);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao criar endereço", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na transação de criar endereço", e);
        }
    }

    public List<Endereco> listarPorCliente(int clienteId) {
        try (Connection conn = DB.getConnection()) {
            EnderecoDao dao = new EnderecoDaoJDBC(conn);
            return dao.findByClienteId(clienteId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar endereços", e);
        }
    }

    public void excluir(int enderecoId, int clienteId) {
        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                EnderecoDao dao = new EnderecoDaoJDBC(conn);
                dao.deleteById(clienteId, enderecoId);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao excluir endereço", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na transação de excluir endereço", e);
        }
    }
}