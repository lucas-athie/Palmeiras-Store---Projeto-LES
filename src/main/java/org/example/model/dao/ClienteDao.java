package org.example.model.dao;

import org.example.model.entities.Cliente;

import java.util.List;

public interface ClienteDao {
    void insert(Cliente cliente);
    void update(Cliente cliente);
    void deleteById(int id);
    Cliente findById(int id);
    List<Cliente> findByFilter(Cliente filtro);
}
