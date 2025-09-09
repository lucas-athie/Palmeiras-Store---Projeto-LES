package org.example.service;

import org.example.config.Database;
import org.example.model.dao.CartaoDao;
import org.example.model.dao.impl.CartaoDaoJDBC;
import org.example.model.entities.Cartao;
import org.example.service.strategy.CartaoValidator;
import org.example.service.strategy.ValidationResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CartaoService {

    private final CartaoValidator validator = new CartaoValidator();

    public void criar(Cartao cartao, int clienteId) {
        ValidationResult vr = validator.validate(cartao);
        vr.assertValid();

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            CartaoDao dao = new CartaoDaoJDBC(conn);
            dao.insert(clienteId, cartao);

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar cartão", e);
        }
    }

    public List<Cartao> listarPorCliente(int clienteId) {
        try (Connection conn = Database.getConnection()) {
            CartaoDao dao = new CartaoDaoJDBC(conn);
            return dao.findByClienteId(clienteId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar cartões", e);
        }
    }

    public void excluir(int cartaoId, int clienteId) {
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            CartaoDao dao = new CartaoDaoJDBC(conn);
            dao.deleteById(clienteId, cartaoId);

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cartão", e);
        }
    }
}