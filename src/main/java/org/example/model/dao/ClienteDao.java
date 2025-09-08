package org.example.model.dao;

import org.example.model.entities.Cliente;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ClienteDao {
    void insert(Cliente cliente);
    void update(Cliente cliente);
    void deleteById(int id);
    Cliente findById(int id);
    List<Cliente> findByFilters(Map<String,String> filters) throws SQLException;

}
