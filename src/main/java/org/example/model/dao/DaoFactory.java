package org.example.model.dao;

import org.example.model.dao.impl.CartaoDaoJDBC;
import org.example.model.dao.impl.ClienteDaoJDBC;
import org.example.model.dao.impl.EnderecoDaoJDBC;

import java.sql.Connection;

public class DaoFactory {
    public static ClienteDao createClienteDao(Connection conn) {
        return new ClienteDaoJDBC(conn);
    }

    public static CartaoDao createCartaoDao(Connection conn) {
        return new CartaoDaoJDBC(conn);
    }

    public static EnderecoDao createEnderecoDao(Connection conn) {
        return new EnderecoDaoJDBC(conn);
    }
}
