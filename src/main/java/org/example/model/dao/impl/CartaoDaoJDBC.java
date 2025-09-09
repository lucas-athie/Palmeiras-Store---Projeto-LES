package org.example.model.dao.impl;

import org.example.model.dao.CartaoDao;
import org.example.model.entities.Bandeira;
import org.example.model.entities.Cartao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CartaoDaoJDBC implements CartaoDao {

    private final Connection conn;

    public CartaoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(int clienteId, Cartao cartao) throws SQLException {
        String sql = """
            INSERT INTO public.cards (
              car_usr_id,
              car_principal,
              car_number,
              car_ccv,
              car_holder,
              car_payment_type
            ) VALUES (?,?,?,?,?,?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, clienteId);
            ps.setBoolean(2, cartao.isPrincipal());
            ps.setString(3, cartao.getNumero());
            ps.setString(4, cartao.getCodigoSeguranca());
            ps.setString(5, cartao.getTitular());
            ps.setString(6, cartao.getBandeira().name());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException(
                        "Falha ao inserir cartão: nenhuma linha afetada."
                );
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cartao.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void deleteById(int clienteId, int cartaoId) throws SQLException {
        String sql = """
            DELETE FROM public.cards
             WHERE car_id = ?
               AND car_usr_id = ?
            """;
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, cartaoId);
            st.setInt(2, clienteId);
            st.executeUpdate();
        }
    }

    @Override
    public List<Cartao> findByClienteId(int clienteId) throws SQLException {
        List<Cartao> lista = new ArrayList<>();
        String sql = """
            SELECT
              car_id,
              car_principal,
              car_number,
              car_ccv,
              car_holder,
              car_payment_type
            FROM public.cards
            WHERE car_usr_id = ?
            """;

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, clienteId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Cartao c = new Cartao();
                    c.setId(rs.getInt("car_id"));
                    c.setPrincipal(rs.getBoolean("car_principal"));
                    c.setNumero(rs.getString("car_number"));
                    c.setCodigoSeguranca(rs.getString("car_ccv"));
                    c.setTitular(rs.getString("car_holder"));
                    c.setBandeira(
                            Bandeira.from(rs.getString("car_payment_type"))
                    );
                    lista.add(c);
                }
            }
        }

        return lista;
    }
}