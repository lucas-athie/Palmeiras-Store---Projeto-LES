package org.example.model.dao;

import org.example.model.entities.Endereco;

import java.util.List;

public interface EnderecoDao {
    void inserir(Endereco endereco);
    void deleteById(int id);
    List<Endereco> findEnderecoByClientId(int id);
}
