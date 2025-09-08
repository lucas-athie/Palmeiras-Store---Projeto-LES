package org.example.model.dao.impl;

import org.example.model.dao.EnderecoDao;
import org.example.model.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class EnderecoDaoJDBC implements EnderecoDao {

    private Connection conn;
    public EnderecoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserir(Endereco endereco, int idCliente) throws SQLException {
        String sql = sqlString();

        if (endereco.getPais() == null || endereco.getPais().getNome() == null) {
            throw new IllegalArgumentException("País é obrigatório");
        }
        if (endereco.getEstado() == null) {
            throw new IllegalArgumentException("Estado é obrigatório");
        }
        if (endereco.getCidade() == null) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }
        if (endereco.getLogradouro() == null || endereco.getLogradouro().getNome() == null) {
            throw new IllegalArgumentException("Logradouro é obrigatório");
        }

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            int i = 1;

            // countries
            st.setString(i++, endereco.getPais().getNome());

            // states
            st.setString(i++, endereco.getEstado().getNome());
            st.setString(i++, endereco.getEstado().getSigla());

            // cities
            st.setString(i++, endereco.getCidade().getNome());

            // street types
            st.setString(i++, endereco.gettLogradouro().name());

            // streets
            st.setString(i++, endereco.getLogradouro().getNome());
            st.setString(i++, endereco.getBairro());
            st.setString(i++, endereco.getCep());

            // residence types
            st.setString(i++, endereco.getResidencia().name());

            // addresses
            st.setInt(i++, Integer.parseInt(endereco.getNumero()));
            st.setString(i++, endereco.getObservacoes());

            // address names
            st.setString(i++, endereco.getApelido());

            // users_addresses
            st.setInt(i++, idCliente);
            st.setString(i++, endereco.getTipoEndereco().name());
            st.setString(i,   endereco.getObservacoes());

            st.executeUpdate();
        }

    }

    @Override
    public void deleteById(int addressId, int idCliente) throws SQLException {
        String delUserAddr =
                "DELETE FROM public.users_addresses ua " +
                        "USING public.addresses_names an " +
                        "WHERE ua.uad_ana_id = an.ana_id " +
                        "  AND an.ana_add_id = ? " +
                        "  AND ua.uad_usr_id = ?";

        String delNames =
                "DELETE FROM public.addresses_names " +
                        "WHERE ana_add_id = ? " +
                        "  AND NOT EXISTS ( " +
                        "    SELECT 1 FROM public.users_addresses ua2 " +
                        "     WHERE ua2.uad_ana_id = addresses_names.ana_id" +
                        "  )";

        String delAddress =
                "DELETE FROM public.addresses " +
                        "WHERE add_id = ? " +
                        "  AND NOT EXISTS ( " +
                        "    SELECT 1 FROM public.addresses_names an2 " +
                        "     WHERE an2.ana_add_id = public.addresses.add_id" +
                        "  )";

        boolean oldAuto = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement st = conn.prepareStatement(delUserAddr)) {
                st.setInt(1, addressId);
                st.setInt(2, idCliente);
                st.executeUpdate();
            }

            try (PreparedStatement st = conn.prepareStatement(delNames)) {
                st.setInt(1, addressId);
                st.executeUpdate();
            }

            try (PreparedStatement st = conn.prepareStatement(delAddress)) {
                st.setInt(1, addressId);
                st.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(oldAuto);
        }
    }

    @Override
    public List<Endereco> findEnderecoByClientId(int idCliente) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();

        String sql =
                "SELECT ua.uad_usr_id           AS user_id,        " +
                        "       an.ana_add_id           AS add_id,         " +
                        "       an.ana_name             AS apelido,        " +
                        "       a.add_number            AS numero,         " +
                        "       a.add_complement        AS complemento,    " +
                        "       rt.rty_residence_type   AS tipo_residencia," +
                        "       ci.cit_city             AS cidade,         " +
                        "       st.sta_state            AS estado,         " +
                        "       st.sta_uf               AS uf,             " +
                        "       co.cou_country          AS pais,           " +
                        "       sty.sty_street_type     AS tipo_logradouro," +
                        "       s.str_street            AS logradouro,     " +
                        "       s.str_neighborhood      AS bairro,         " +
                        "       s.str_zip               AS cep,            " +
                        "       ua.uad_type             AS tipo_endereco,  " +
                        "       ua.uad_observacoes      AS observacoes     " +
                        "FROM public.users_addresses ua " +
                        "JOIN public.addresses_names    an ON ua.uad_ana_id  = an.ana_id  " +
                        "JOIN public.addresses          a  ON an.ana_add_id  = a.add_id   " +
                        "JOIN public.residence_types    rt ON a.add_rty_id   = rt.rty_id  " +
                        "JOIN public.cities             ci ON a.add_cit_id   = ci.cit_id  " +
                        "JOIN public.states             st ON ci.cit_sta_id  = st.sta_id  " +
                        "JOIN public.countries          co ON st.sta_cou_id  = co.cou_id  " +
                        "JOIN public.streets            s  ON a.add_str_id   = s.str_id   " +
                        "JOIN public.street_types       sty ON s.str_sty_id   = sty.sty_id " +
                        "WHERE ua.uad_usr_id = ?         " +
                        "ORDER BY an.ana_add_id";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idCliente);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("user_id"));

                    Endereco endereco = new Endereco();
                    endereco.setIdEndereco(     rs.getInt("add_id"));
                    endereco.setApelido(        rs.getString("apelido"));
                    endereco.setNumero(         Integer.toString(rs.getInt("numero")));
                    endereco.setObservacoes(    rs.getString("complemento"));
                    endereco.setResidencia(     TResidencia.from(rs.getString("tipo_residencia")));
                    endereco.setCidade(         new Cidade(rs.getString("cidade")));
                    endereco.setEstado(         new Estado(rs.getString("estado"), rs.getString("uf")));
                    endereco.setPais(           new Pais(rs.getString("pais")));
                    endereco.settLogradouro(    TLogradouro.from(rs.getString("tipo_logradouro")));
                    endereco.setLogradouro(     new Logradouro(rs.getString("logradouro")));
                    endereco.setBairro(         rs.getString("bairro"));
                    endereco.setCep(            rs.getString("cep"));
                    endereco.setTipoEndereco(   TEndereco.from(rs.getString("tipo_endereco")));

                    enderecos.add(endereco);
                }
            }
        }

        return enderecos;
    }

    public String sqlString(){
        String sql =
                "WITH ins_country AS ( " +
                        "    INSERT INTO public.countries(cou_country) " +
                        "    VALUES(?) RETURNING cou_id" +
                        "), ins_state AS ( " +
                        "    INSERT INTO public.states(sta_state, sta_uf, sta_cou_id) " +
                        "    VALUES(?, ?, (SELECT cou_id FROM ins_country)) RETURNING sta_id" +
                        "), ins_city AS ( " +
                        "    INSERT INTO public.cities(cit_city, cit_sta_id) " +
                        "    VALUES(?, (SELECT sta_id FROM ins_state)) RETURNING cit_id" +
                        "), ins_street_type AS ( " +
                        "    INSERT INTO public.street_types(sty_street_type) " +
                        "    VALUES(?) RETURNING sty_id" +
                        "), ins_street AS ( " +
                        "    INSERT INTO public.streets(str_street, str_neighborhood, str_zip, str_sty_id) " +
                        "    VALUES(?, ?, ?, (SELECT sty_id FROM ins_street_type)) RETURNING str_id" +
                        "), ins_residence_type AS ( " +
                        "    INSERT INTO public.residence_types(rty_residence_type) " +
                        "    VALUES(?) RETURNING rty_id" +
                        "), ins_address AS ( " +
                        "    INSERT INTO public.addresses(add_number, add_complement, add_rty_id, add_cit_id, add_str_id) " +
                        "    VALUES(?, ?, " +
                        "           (SELECT rty_id FROM ins_residence_type), " +
                        "           (SELECT cit_id FROM ins_city), " +
                        "           (SELECT str_id FROM ins_street)) RETURNING add_id" +
                        "), ins_name AS ( " +
                        "    INSERT INTO public.addresses_names(ana_name, ana_add_id) " +
                        "    VALUES(?, (SELECT add_id FROM ins_address)) RETURNING ana_id" +
                        ") " +
                        "INSERT INTO public.users_addresses(uad_usr_id, uad_ana_id, uad_type, uad_observacoes) " +
                        "VALUES(?, (SELECT ana_id FROM ins_name), ?, ?)";

        return sql;
    }
}
