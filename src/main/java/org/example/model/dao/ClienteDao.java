package org.example.model.dao;

import org.example.model.entities.Cliente;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ClienteDao {

    void insert(Cliente cliente) throws SQLException;

    void update(Cliente cliente) throws SQLException;

    void deleteById(int id) throws SQLException;

    Cliente findById(int id) throws SQLException;

    List<Cliente> findAll() throws SQLException;

    List<Cliente> findBySearch(String termo) throws SQLException;


    List<Cliente> findByFilters(Map<String, String> filters) throws SQLException;
}
