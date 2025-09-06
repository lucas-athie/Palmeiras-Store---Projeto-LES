package org.example.model.dao.impl;

import org.example.model.dao.EnderecoDao;
import org.example.model.entities.Endereco;

import java.util.List;

public class EnderecoDaoJDBC implements EnderecoDao {
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
