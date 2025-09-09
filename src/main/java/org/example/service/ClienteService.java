package org.example.service;

import org.example.db.DB;
import org.example.model.dao.ClienteDao;
import org.example.model.dao.EnderecoDao;
import org.example.model.dao.CartaoDao;
import org.example.model.dao.impl.ClienteDaoJDBC;
import org.example.model.dao.impl.EnderecoDaoJDBC;
import org.example.model.dao.impl.CartaoDaoJDBC;
import org.example.model.entities.Cliente;
import org.example.model.entities.Endereco;
import org.example.model.entities.Cartao;
import org.example.service.strategy.CodigoStrategy;
import org.example.service.strategy.CriptoStrategy;
import org.example.service.strategy.ValidationResult;
import org.example.service.strategy.ValidationStrategy;
import org.example.service.strategy.ClienteValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ClienteService {

    private final CriptoStrategy cripto;
    private final CodigoStrategy codigoStrategy;
    private final ValidationStrategy<Cliente> createValidator;
    private final ValidationStrategy<Cliente> updateValidator;

    public ClienteService(CriptoStrategy cripto, CodigoStrategy codigoStrategy) {
        this.cripto          = cripto;
        this.codigoStrategy  = codigoStrategy;
        this.createValidator = new ClienteValidator();
        this.updateValidator = new BasicClienteValidator();
    }

    public void cadastrar(Cliente cliente) {
        cliente.setCodigo(codigoStrategy.gerarCodigo());
        ValidationResult vr = createValidator.validate(cliente);
        vr.assertValid();

        cliente.setSenha(cripto.encrypt(cliente.getSenha()));

        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                ClienteDao clienteDao = new ClienteDaoJDBC(conn);
                clienteDao.insert(cliente);
                int userId = cliente.getIdCliente();

                EnderecoDao enderecoDao = new EnderecoDaoJDBC(conn);
                for (Endereco e : cliente.getEnderecos()) {
                    enderecoDao.insert(userId, e);
                }

                CartaoDao cartaoDao = new CartaoDaoJDBC(conn);
                for (Cartao c : cliente.getCartoes()) {
                    cartaoDao.insert(userId, c);
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(
                        "Erro ao cadastrar cliente: SQLState="
                                + e.getSQLState() + " – " + e.getMessage(), e
                );
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na transação de cadastro", e);
        }
    }

    public void atualizar(Cliente cliente) {
        ValidationResult vr = updateValidator.validate(cliente);
        vr.assertValid();

        if (cliente.getSenha() != null && !cliente.getSenha().isEmpty()) {
            cliente.setSenha(cripto.encrypt(cliente.getSenha()));
        }

        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                ClienteDao dao = new ClienteDaoJDBC(conn);
                dao.update(cliente);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(
                        "Erro ao atualizar cliente: SQLState="
                                + e.getSQLState() + " – " + e.getMessage(), e
                );
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na transação de atualização", e);
        }
    }

    public void deletar(int idCliente) {
        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                ClienteDao dao = new ClienteDaoJDBC(conn);
                dao.deleteById(idCliente);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao excluir cliente", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na transação de exclusão", e);
        }
    }

    public Cliente buscarPorId(int idCliente) {
        try (Connection conn = DB.getConnection()) {
            ClienteDao clienteDao = new ClienteDaoJDBC(conn);
            Cliente cliente = clienteDao.findById(idCliente);

            if (cliente != null) {
                EnderecoDao enderecoDao = new EnderecoDaoJDBC(conn);
                cliente.setEnderecos(enderecoDao.findByClienteId(idCliente));

                CartaoDao cartaoDao = new CartaoDaoJDBC(conn);
                cliente.setCartoes(cartaoDao.findByClienteId(idCliente));
            }

            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID", e);
        }
    }

    public List<Cliente> listarTodos() {
        try (Connection conn = DB.getConnection()) {
            ClienteDao dao = new ClienteDaoJDBC(conn);
            List<Cliente> clientes = dao.findAll();

            EnderecoDao enderecoDao = new EnderecoDaoJDBC(conn);
            CartaoDao cartaoDao     = new CartaoDaoJDBC(conn);
            for (Cliente cliente : clientes) {
                int id = cliente.getIdCliente();
                cliente.setEnderecos(enderecoDao.findByClienteId(id));
                cliente.setCartoes(cartaoDao.findByClienteId(id));
            }
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os clientes", e);
        }
    }

    public List<Cliente> listarPorPesquisa(String termo) {
        try (Connection conn = DB.getConnection()) {
            ClienteDao dao = new ClienteDaoJDBC(conn);
            List<Cliente> clientes = dao.findBySearch(termo);

            EnderecoDao enderecoDao = new EnderecoDaoJDBC(conn);
            CartaoDao cartaoDao     = new CartaoDaoJDBC(conn);
            for (Cliente cliente : clientes) {
                int id = cliente.getIdCliente();
                cliente.setEnderecos(enderecoDao.findByClienteId(id));
                cliente.setCartoes(cartaoDao.findByClienteId(id));
            }
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes por pesquisa", e);
        }
    }

    public List<Cliente> listarPorFiltros(Map<String, String> filtros) {
        try (Connection conn = DB.getConnection()) {
            ClienteDao dao = new ClienteDaoJDBC(conn);
            List<Cliente> clientes = dao.findByFilters(filtros);

            EnderecoDao enderecoDao = new EnderecoDaoJDBC(conn);
            CartaoDao cartaoDao     = new CartaoDaoJDBC(conn);
            for (Cliente cliente : clientes) {
                int id = cliente.getIdCliente();
                cliente.setEnderecos(enderecoDao.findByClienteId(id));
                cliente.setCartoes(cartaoDao.findByClienteId(id));
            }
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes", e);
        }
    }

    private static class BasicClienteValidator
            implements ValidationStrategy<Cliente> {
        @Override
        public ValidationResult validate(Cliente c) {
            ValidationResult vr = new ValidationResult();
            if (c.getNome() == null || c.getNome().isBlank()) {
                vr.addError("Nome é obrigatório");
            }
            if (c.getEmail() == null || c.getEmail().isBlank()) {
                vr.addError("Email é obrigatório");
            }
            if (c.getCpf() == null || c.getCpf().isBlank()) {
                vr.addError("CPF é obrigatório");
            }
            if (c.getDataNascimento() == null) {
                vr.addError("Data de nascimento é obrigatória");
            }
            if (c.getTelefone() == null
                    || c.getTelefone().getNumero() == null
                    || c.getTelefone().getNumero().isBlank()) {
                vr.addError("Telefone é obrigatório");
            }
            if (c.getGenero() == null) {
                vr.addError("Gênero é obrigatório");
            }
            return vr;
        }
    }
}
