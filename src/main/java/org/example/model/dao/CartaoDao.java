package org.example.model.dao;

import org.example.model.entities.Cartao;

import java.util.List;

public interface CartaoDao {

    void insert (Cartao cartao);
    void deleteByid (int id);
    List<Cartao> findCartaoByClienteId(int idCliente);

}
