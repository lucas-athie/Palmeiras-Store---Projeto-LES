package org.example.model.dao.impl;

import org.example.model.dao.EnderecoDao;
import org.example.model.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EnderecoDaoJDBC implements EnderecoDao {

    private final Connection conn;

    public EnderecoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(int clienteId, Endereco endereco) throws SQLException {
        String sql = sqlString();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, endereco.getPais().getNome());
            ps.setString(i++, endereco.getEstado().getNome());
            ps.setString(i++, endereco.getEstado().getSigla());
            ps.setString(i++, endereco.getCidade().getNome());
            ps.setString(i++, endereco.getTipoLogradouro().name());
            ps.setString(i++, endereco.getLogradouro().getNome());
            ps.setString(i++, endereco.getBairro());
            ps.setString(i++, endereco.getCep());
            ps.setString(i++, endereco.getTipoResidencia().name());
            ps.setString(i++, endereco.getNumero());
            ps.setString(i++, endereco.getObservacoes());
            ps.setString(i++, endereco.getApelido());
            ps.setInt   (i++, clienteId);
            ps.setString(i++, endereco.getTipoEndereco().name());
            ps.setString(i++, endereco.getObservacoes());

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(int clienteId, int enderecoId) throws SQLException {
        String sqlDelUserAddr = """
            DELETE FROM public.users_addresses ua
              USING public.addresses_names an
             WHERE ua.uad_ana_id   = an.ana_id
               AND an.ana_add_id  = ?
               AND ua.uad_usr_id  = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sqlDelUserAddr)) {
            ps.setInt(1, enderecoId);
            ps.setInt(2, clienteId);
            ps.executeUpdate();
        }

        // 2) remove names órfãos
        String sqlDelNames = """
            DELETE FROM public.addresses_names
             WHERE ana_add_id = ?
               AND NOT EXISTS (
                 SELECT 1
                   FROM public.users_addresses ua2
                  WHERE ua2.uad_ana_id = addresses_names.ana_id
               )
            """;
        try (PreparedStatement ps = conn.prepareStatement(sqlDelNames)) {
            ps.setInt(1, enderecoId);
            ps.executeUpdate();
        }

        String sqlDelAddress = """
            DELETE FROM public.addresses
             WHERE add_id = ?
               AND NOT EXISTS (
                 SELECT 1
                   FROM public.addresses_names an2
                  WHERE an2.ana_add_id = public.addresses.add_id
               )
            """;
        try (PreparedStatement ps = conn.prepareStatement(sqlDelAddress)) {
            ps.setInt(1, enderecoId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Endereco> findByClienteId(int clienteId) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = """
            SELECT
              an.ana_add_id           AS add_id,
              an.ana_name             AS apelido,
              a.add_number            AS numero,
              a.add_complement        AS complemento,
              rt.rty_residence_type   AS tipo_residencia,
              ci.cit_city             AS cidade,
              st.sta_state            AS estado,
              st.sta_uf               AS uf,
              co.cou_country          AS pais,
              sty.sty_street_type     AS tipo_logradouro,
              s.str_street            AS logradouro,
              s.str_neighborhood      AS bairro,
              s.str_zip               AS cep,
              ua.uad_type             AS tipo_endereco
            FROM public.users_addresses ua
            JOIN public.addresses_names   an  ON ua.uad_ana_id  = an.ana_id
            JOIN public.addresses         a   ON an.ana_add_id  = a.add_id
            JOIN public.residence_types   rt  ON a.add_rty_id   = rt.rty_id
            JOIN public.cities            ci  ON a.add_cit_id   = ci.cit_id
            JOIN public.states            st  ON ci.cit_sta_id  = st.sta_id
            JOIN public.countries         co  ON st.sta_cou_id  = co.cou_id
            JOIN public.streets           s   ON a.add_str_id   = s.str_id
            JOIN public.street_types      sty ON s.str_sty_id   = sty.sty_id
            WHERE ua.uad_usr_id = ?
            ORDER BY an.ana_add_id
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Endereco e = new Endereco();
                    e.setIdEndereco(      rs.getInt("add_id"));
                    e.setApelido(         rs.getString("apelido"));
                    e.setNumero(          rs.getString("numero"));
                    e.setObservacoes(     rs.getString("complemento"));
                    e.setTipoResidencia(  TResidencia.from(rs.getString("tipo_residencia")));
                    e.setCidade(          new Cidade(rs.getString("cidade")));
                    e.setEstado(          new Estado(
                            rs.getString("estado"),
                            rs.getString("uf")
                    ));
                    e.setPais(            new Pais(rs.getString("pais")));
                    e.setTipoLogradouro(  TLogradouro.from(rs.getString("tipo_logradouro")));
                    e.setLogradouro(      new Logradouro(rs.getString("logradouro")));
                    e.setBairro(          rs.getString("bairro"));
                    e.setCep(             rs.getString("cep"));
                    e.setTipoEndereco(    TEndereco.from(rs.getString("tipo_endereco")));
                    enderecos.add(e);
                }
            }
        }

        return enderecos;
    }


    private String sqlString() {
        return """
            WITH ins_country AS (
              INSERT INTO public.countries(cou_country)
                VALUES (?)
              RETURNING cou_id
            ), ins_state AS (
              INSERT INTO public.states(sta_state, sta_uf, sta_cou_id)
                VALUES (?, ?, (SELECT cou_id FROM ins_country))
              RETURNING sta_id
            ), ins_city AS (
              INSERT INTO public.cities(cit_city, cit_sta_id)
                VALUES (?, (SELECT sta_id FROM ins_state))
              RETURNING cit_id
            ), ins_street_type AS (
              INSERT INTO public.street_types(sty_street_type)
                VALUES (?)
              RETURNING sty_id
            ), ins_street AS (
              INSERT INTO public.streets(
                  str_street, str_neighborhood, str_zip, str_sty_id
              )
                VALUES (?, ?, ?, (SELECT sty_id FROM ins_street_type))
              RETURNING str_id
            ), ins_residence AS (
              INSERT INTO public.residence_types(rty_residence_type)
                VALUES (?)
              RETURNING rty_id
            ), ins_address AS (
              INSERT INTO public.addresses(
                  add_number, add_complement, add_rty_id, add_cit_id, add_str_id
              )
                VALUES (
                  ?, ?,
                  (SELECT rty_id FROM ins_residence),
                  (SELECT cit_id FROM ins_city),
                  (SELECT str_id FROM ins_street)
                )
              RETURNING add_id
            ), ins_name AS (
              INSERT INTO public.addresses_names(ana_add_id, ana_name)
                VALUES((SELECT add_id FROM ins_address), ?)
              RETURNING ana_id
            )
            INSERT INTO public.users_addresses(
              uad_usr_id, uad_ana_id, uad_type, uad_observacoes
            )
            VALUES(
              ?, (SELECT ana_id FROM ins_name), ?, ?
            )
            """;
    }
}