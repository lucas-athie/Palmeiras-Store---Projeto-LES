package org.example.model.dao.impl;

import org.example.model.dao.EnderecoDao;
import org.example.model.entities.Endereco;

import java.sql.Connection;
import java.util.List;

public class EnderecoDaoJDBC implements EnderecoDao {
    public EnderecoDaoJDBC(Connection conn) {
    }

    @Override
    public void inserir(Endereco endereco) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<Endereco> findEnderecoByClientId(int id) {
        return List.of();
    }
}
