package org.example.model.dao;

import org.example.model.dao.impl.CartaoDaoJDBC;
import org.example.model.dao.impl.ClienteDaoJDBC;
import org.example.model.dao.impl.EnderecoDaoJDBC;

import java.sql.Connection;

public class DaoFactory {
    public static ClienteDao createClienteDao() {
        return new ClienteDaoJDBC();
    }
    public static CartaoDao createCartaoDao() {
        return new CartaoDaoJDBC();
    }
    public static EnderecoDao createEnderecoDao() {
        return new EnderecoDaoJDBC();
    }
}
