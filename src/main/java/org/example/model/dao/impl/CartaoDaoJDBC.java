package org.example.model.dao.impl;

import org.example.model.dao.CartaoDao;
import org.example.model.entities.Bandeira;
import org.example.model.entities.Cartao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartaoDaoJDBC implements CartaoDao {

    private Connection conn;
    public CartaoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Cartao cartao, int idCliente) {
        String sql = "INSERT INTO public.cards (" +
                "  car_usr_id, car_principal, car_number, car_ccv, car_holder, car_valid, car_payment_type" +
                ") VALUES (?,?,?,?,?,?,?) RETURNING car_id";

        try(PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idCliente);
            st.setBoolean(2, cartao.getPrincipal());
            st.setString(3, cartao.getNumero());
            st.setString(4, cartao.getCodigoSeguranca());
            st.setString(5, cartao.getTitular());
            st.setDate(6, new java.sql.Date(0L));
            st.setString(7, cartao.getBandeira().getLabel());

            try(ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    cartao.setId(rs.getInt("car_id"));
                }
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteByid(int id) {
        String sql = "DELETE FROM public.cards WHERE car_id = ?";

        try(PreparedStatement st = conn.prepareStatement(sql)){
            st.setInt(1, id);
            st.executeUpdate();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cartao> findCartaoByClienteId(int idCliente) throws SQLException {

        List<Cartao> cartaos = new ArrayList<>();

        String sql =  "SELECT " +
                "  car_id, " +
                "  car_principal, " +
                "  car_number, " +
                "  car_ccv, " +
                "  car_holder, " +
                "  car_valid, " +
                "  car_payment_type " +
                "FROM public.cards " +
                "WHERE car_usr_id = ?";

    try(PreparedStatement st = conn.prepareStatement(sql)){
        st.setInt(1, idCliente);
        try(ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Cartao cartao = new Cartao();
                cartao.setId(rs.getInt("car_id"));
                cartao.setPrincipal(rs.getBoolean("car_principal"));
                cartao.setNumero(rs.getString("car_number"));
                cartao.setCodigoSeguranca(rs.getString("car_ccv"));
                cartao.setTitular(rs.getString("car_holder"));
                cartao.setBandeira(Bandeira.from(rs.getString("car_payment_type")));

                cartaos.add(cartao);

            }
        }
    }catch (Exception e) {
        e.printStackTrace();
    }
        return cartaos;
    }
}
