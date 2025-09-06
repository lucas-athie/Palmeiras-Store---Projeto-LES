package org.example.model.dao.impl;

import org.example.model.dao.ClienteDao;
import org.example.model.entities.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.example.db.DB;

public class ClienteDaoJDBC implements ClienteDao {

    private Connection conn;

    public ClienteDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Cliente cliente) {
        PreparedStatement st = null;

        try{
            st = conn.prepareStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Cliente cliente) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Cliente findById(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sqlUser =
                    "SELECT u.usr_id, u.usr_code, u.usr_name, u.usr_email, u.usr_active, " +
                            "       u.usr_birthday, u.usr_cpf, u.usr_phone_type, u.usr_phone_ddd, " +
                            "       u.usr_phone_number, u.usr_ranking, g.gen_name AS genero, " +
                            "       u.usr_createdAt, u.usr_updatedAt, u.usr_publishedAt " +
                            "FROM users u " +
                            "JOIN genders g ON g.gen_id = u.usr_gen_id " +
                            "WHERE u.usr_id = ?";

            st = conn.prepareStatement(sqlUser);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente();
            }
            return null;

        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public List<Cliente> findByFilter(Cliente filtro) {
        return List.of();
    }
}
