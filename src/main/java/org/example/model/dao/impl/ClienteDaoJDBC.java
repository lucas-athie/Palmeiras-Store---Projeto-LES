package org.example.model.dao.impl;

import org.example.model.dao.ClienteDao;
import org.example.model.entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClienteDaoJDBC implements ClienteDao {

    private final Connection conn;

    public ClienteDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Cliente cliente) throws SQLException {
        String sql = """
            INSERT INTO public.users (
              usr_code,
              usr_name,
              usr_email,
              usr_active,
              usr_birthday,
              usr_cpf,
              usr_gen_id,
              usr_phone_type,
              usr_phone_ddd,
              usr_phone_number,
              usr_password
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int idx = 1;
            ps.setObject(idx++, UUID.fromString(cliente.getCodigo()), java.sql.Types.OTHER);
            ps.setString(idx++, cliente.getNome());
            ps.setString(idx++, cliente.getEmail());
            ps.setBoolean(idx++, cliente.isAtivo());
            ps.setDate(idx++, new java.sql.Date(cliente.getDataNascimento().getTime()));
            ps.setString(idx++, cliente.getCpf());
            ps.setInt(idx++, cliente.getGenero().getId());

            Telefone tel = cliente.getTelefone();
            ps.setString(idx++, tel.getTipo().name());
            ps.setString(idx++, tel.getDdd());
            ps.setString(idx++, tel.getNumero());

            ps.setString(idx, cliente.getSenha());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Falha ao inserir Cliente: nenhuma linha afetada.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Cliente cliente) throws SQLException {
        String sql = """
            UPDATE public.users SET
              usr_name = ?,
              usr_email = ?,
              usr_active = ?,
              usr_birthday = ?,
              usr_cpf = ?,
              usr_gen_id = ?,
              usr_phone_type = ?,
              usr_phone_ddd = ?,
              usr_phone_number = ?,
              usr_password = ?
            WHERE usr_id = ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.setBoolean(3, cliente.isAtivo());
            ps.setDate(4, new java.sql.Date(cliente.getDataNascimento().getTime()));
            ps.setString(5, cliente.getCpf());
            ps.setInt(6, cliente.getGenero().getId());

            Telefone tel = cliente.getTelefone();
            ps.setString(7, tel.getTipo().name());
            ps.setString(8, tel.getDdd());
            ps.setString(9, tel.getNumero());

            ps.setString(10, cliente.getSenha());
            ps.setInt(11, cliente.getIdCliente());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Falha ao atualizar Cliente: nenhuma linha afetada.");
            }
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        try (PreparedStatement ps1 = conn.prepareStatement(
                "DELETE FROM public.cards WHERE car_usr_id = ?")) {
            ps1.setInt(1, id);
            ps1.executeUpdate();
        }
        try (PreparedStatement ps2 = conn.prepareStatement(
                "DELETE FROM public.users_addresses WHERE uad_usr_id = ?")) {
            ps2.setInt(1, id);
            ps2.executeUpdate();
        }
        try (PreparedStatement ps3 = conn.prepareStatement(
                "DELETE FROM public.users WHERE usr_id = ?")) {
            ps3.setInt(1, id);
            int affected = ps3.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Nenhum Cliente encontrado com id = " + id);
            }
        }
    }

    @Override
    public Cliente findById(int id) throws SQLException {
        String sql = """
            SELECT
              u.usr_id,
              u.usr_code,
              u.usr_name,
              u.usr_email,
              u.usr_active,
              u.usr_birthday,
              u.usr_cpf,
              u.usr_password,
              g.gen_id AS gen_id,
              u.usr_phone_type,
              u.usr_phone_ddd,
              u.usr_phone_number,
              c.car_id,
              c.car_principal,
              c.car_number,
              c.car_ccv,
              c.car_holder,
              c.car_payment_type,
              ua.uad_type AS endereco_tipo,
              an.ana_name AS endereco_apelido,
              ua.uad_observacoes AS endereco_obs,
              a.add_number AS endereco_num,
              a.add_complement AS endereco_comp,
              rt.rty_residence_type AS residencia_tipo,
              ci.cit_city AS cidade_nome,
              st.sta_state AS estado_nome,
              st.sta_uf AS estado_uf,
              co.cou_country AS pais_nome,
              s.str_street AS logradouro_nome,
              s.str_neighborhood AS logradouro_bairro,
              s.str_zip AS logradouro_cep,
              sty.sty_street_type AS logradouro_tipo
            FROM public.users u
            JOIN public.genders g
              ON g.gen_id = u.usr_gen_id
            LEFT JOIN public.cards c
              ON c.car_usr_id = u.usr_id
            LEFT JOIN public.users_addresses ua
              ON ua.uad_usr_id = u.usr_id
            LEFT JOIN public.addresses_names an
              ON an.ana_id = ua.uad_ana_id
            LEFT JOIN public.addresses a
              ON a.add_id = an.ana_add_id
            LEFT JOIN public.residence_types rt
              ON rt.rty_id = a.add_rty_id
            LEFT JOIN public.cities ci
              ON ci.cit_id = a.add_cit_id
            LEFT JOIN public.states st
              ON st.sta_id = ci.cit_sta_id
            LEFT JOIN public.countries co
              ON co.cou_id = st.sta_cou_id
            LEFT JOIN public.streets s
              ON s.str_id = a.add_str_id
            LEFT JOIN public.street_types sty
              ON sty.sty_id = s.str_sty_id
            WHERE u.usr_id = ?
            """;

        Cliente cliente = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (cliente == null) {
                        cliente = new Cliente();
                        cliente.setIdCliente(rs.getInt("usr_id"));
                        UUID uuid = rs.getObject("usr_code", UUID.class);
                        cliente.setCodigo(uuid.toString());

                        cliente.setNome(rs.getString("usr_name"));
                        cliente.setEmail(rs.getString("usr_email"));
                        cliente.setAtivo(rs.getBoolean("usr_active"));
                        cliente.setDataNascimento(rs.getDate("usr_birthday"));
                        cliente.setCpf(rs.getString("usr_cpf"));
                        cliente.setSenha(rs.getString("usr_password"));
                        cliente.setGenero(Genero.fromId(rs.getInt("gen_id")));

                        TTelefone tipoTel = TTelefone.from(rs.getString("usr_phone_type"));
                        cliente.setTelefone(new Telefone(
                                rs.getString("usr_phone_number"),
                                rs.getString("usr_phone_ddd"),
                                tipoTel
                        ));
                    }

                    Long cardId = rs.getObject("car_id", Long.class);
                    if (cardId != null) {
                        Cartao cartao = new Cartao();
                        cartao.setId(cardId.intValue());
                        cartao.setPrincipal(rs.getBoolean("car_principal"));
                        cartao.setNumero(rs.getString("car_number"));
                        cartao.setCodigoSeguranca(rs.getString("car_ccv"));
                        cartao.setTitular(rs.getString("car_holder"));
                        cartao.setBandeira(Bandeira.from(rs.getString("car_payment_type")));
                        cliente.addCartao(cartao);
                    }

                    String apelido = rs.getString("endereco_apelido");
                    if (apelido != null) {
                        Endereco end = new Endereco();
                        end.setApelido(apelido);
                        end.setObservacoes(rs.getString("endereco_obs"));
                        end.setNumero(rs.getString("endereco_num"));
                        end.setTipoResidencia(TResidencia.from(rs.getString("residencia_tipo")));
                        end.setCidade(new Cidade(rs.getString("cidade_nome")));
                        end.setEstado(new Estado(
                                rs.getString("estado_nome"),
                                rs.getString("estado_uf")
                        ));
                        end.setPais(new Pais(rs.getString("pais_nome")));
                        end.setCep(rs.getString("logradouro_cep"));
                        end.setTipoLogradouro(TLogradouro.from(rs.getString("logradouro_tipo")));
                        end.setLogradouro(new Logradouro(rs.getString("logradouro_nome")));
                        end.setBairro(rs.getString("logradouro_bairro"));
                        end.setTipoEndereco(TEndereco.from(rs.getString("endereco_tipo")));
                        cliente.addEndereco(end);
                    }
                }
            }
        }
        return cliente;
    }

    @Override
    public List<Cliente> findAll() throws SQLException {
        return findByFilters(Collections.emptyMap());
    }

    @Override
    public List<Cliente> findBySearch(String termo) throws SQLException {
        return findByFilters(Map.of("search", termo));
    }

    @Override
    public List<Cliente> findByFilters(Map<String, String> filters) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT
              u.usr_id,
              u.usr_code,
              u.usr_name,
              u.usr_email,
              u.usr_active,
              u.usr_birthday,
              u.usr_cpf,
              u.usr_phone_type,
              u.usr_phone_ddd,
              u.usr_phone_number,
              g.gen_id AS gen_id
            FROM public.users u
            JOIN public.genders g
              ON g.gen_id = u.usr_gen_id
            WHERE 1=1
            """);
        List<Object> params = new ArrayList<>();

        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("search")) {
                sql.append("""
                  AND (
                    u.usr_name  ILIKE ? OR
                    u.usr_email ILIKE ? OR
                    u.usr_cpf   ILIKE ?
                  )
                  """);
                String like = "%" + filters.get("search").trim() + "%";
                params.add(like);
                params.add(like);
                params.add(like);
            }
            if (filters.containsKey("nome")) {
                sql.append(" AND u.usr_name ILIKE ?");
                params.add("%" + filters.get("nome").trim() + "%");
            }
            if (filters.containsKey("email")) {
                sql.append(" AND u.usr_email ILIKE ?");
                params.add("%" + filters.get("email").trim() + "%");
            }
            if (filters.containsKey("cpf")) {
                sql.append(" AND u.usr_cpf ILIKE ?");
                params.add("%" + filters.get("cpf").trim() + "%");
            }
            if (filters.containsKey("usr_id")) {
                sql.append(" AND u.usr_id = ?");
                params.add(Integer.parseInt(filters.get("usr_id")));
            }
        }

        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRowBasic(rs));
                }
            }
        }
        return lista;
    }

    private Cliente mapRowBasic(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("usr_id"));
        c.setCodigo(rs.getString("usr_code"));
        c.setNome(rs.getString("usr_name"));
        c.setEmail(rs.getString("usr_email"));
        c.setAtivo(rs.getBoolean("usr_active"));
        c.setDataNascimento(rs.getDate("usr_birthday"));
        c.setCpf(rs.getString("usr_cpf"));
        c.setGenero(Genero.fromId(rs.getInt("gen_id")));

        String tipo = rs.getString("usr_phone_type");
        String ddd  = rs.getString("usr_phone_ddd");
        String num  = rs.getString("usr_phone_number");
        c.setTelefone(new Telefone(num, ddd, TTelefone.from(tipo)));

        return c;
    }
}