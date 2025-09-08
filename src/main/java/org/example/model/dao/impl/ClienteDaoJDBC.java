package org.example.model.dao.impl;

import org.example.model.dao.ClienteDao;
import org.example.model.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteDaoJDBC implements ClienteDao {

    private Connection conn;

    public ClienteDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Cliente cliente) {
        // implementar
    }

    @Override
    public void update(Cliente cliente) {
        // implementar
    }

    @Override
    public void deleteById(int id) {
        // implementar
    }

    @Override
    public Cliente findById(int id) {
        ResultSet rs = null;

        try {
            String sql =
                    "SELECT\n" +
                            "  u.usr_id,\n" +
                            "  u.usr_code,\n" +
                            "  u.usr_name,\n" +
                            "  u.usr_email,\n" +
                            "  u.usr_active,\n" +
                            "  u.usr_birthday,\n" +
                            "  u.usr_cpf,\n" +
                            "  u.usr_phone_type,\n" +
                            "  u.usr_phone_ddd,\n" +
                            "  u.usr_phone_number,\n" +
                            "  u.usr_ranking,\n" +
                            "  g.gen_name AS genero,\n" +
                            "  u.usr_createdAt,\n" +
                            "  u.usr_updatedAt,\n" +
                            "  u.usr_publishedAt,\n" +
                            "  c.car_id,\n" +
                            "  c.car_principal,\n" +
                            "  c.car_number,\n" +
                            "  c.car_ccv,\n" +
                            "  c.car_holder,\n" +
                            "  c.car_valid,\n" +
                            "  c.car_payment_type,\n" +
                            "  ua.uad_type,\n" +
                            "  ua.uad_observacoes,\n" +
                            "  an.ana_name,\n" +
                            "  a.add_number,\n" +
                            "  a.add_complement,\n" +
                            "  rt.rty_residence_type,\n" +
                            "  ci.cit_city,\n" +
                            "  st.sta_state,\n" +
                            "  st.sta_uf,\n" +
                            "  co.cou_country,\n" +
                            "  s.str_street,\n" +
                            "  s.str_neighborhood,\n" +
                            "  s.str_zip,\n" +
                            "  sty.sty_street_type\n" +
                            "FROM public.users u\n" +
                            "JOIN public.genders g ON g.gen_id = u.usr_gen_id\n" +
                            "LEFT JOIN public.cards c ON c.car_usr_id = u.usr_id\n" +
                            "LEFT JOIN public.users_addresses ua ON ua.uad_usr_id = u.usr_id\n" +
                            "LEFT JOIN public.addresses_names an ON an.ana_id = ua.uad_ana_id\n" +
                            "LEFT JOIN public.addresses a ON a.add_id = an.ana_add_id\n" +
                            "LEFT JOIN public.residence_types rt ON rt.rty_id = a.add_rty_id\n" +
                            "LEFT JOIN public.cities ci ON ci.cit_id = a.add_cit_id\n" +
                            "LEFT JOIN public.states st ON st.sta_id = ci.cit_sta_id\n" +
                            "LEFT JOIN public.countries co ON co.cou_id = st.sta_cou_id\n" +
                            "LEFT JOIN public.streets s ON s.str_id = a.add_str_id\n" +
                            "LEFT JOIN public.street_types sty ON sty.sty_id = s.str_sty_id\n" +
                            "WHERE u.usr_id = ?";

            Map<Integer, Cliente> cache = new HashMap<>();

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, id);
                rs = st.executeQuery();

                while (rs.next()) {
                    int userId = rs.getInt("usr_id");

                    Cliente cliente = cache.get(userId);
                    if (cliente == null) {
                        cliente = new Cliente();
                        cliente.setIdCliente(userId);
                        cliente.setCodigo(rs.getString("usr_code"));
                        cliente.setNome(rs.getString("usr_name"));
                        cliente.setEmail(rs.getString("usr_email"));
                        cliente.setAtivo(rs.getBoolean("usr_active"));
                        cliente.setDataNascimento(rs.getDate("usr_birthday"));
                        cliente.setCpf(rs.getString("usr_cpf"));
                        cliente.setRank(rs.getString("usr_ranking"));

                        // genero
                        String rawGenero = rs.getString("genero");
                        cliente.setGenero(Genero.from(rawGenero));

                        // telefone
                        String ddd = rs.getString("usr_phone_ddd");
                        String number = rs.getString("usr_phone_number");
                        TTelefone tipo = TTelefone.from(rs.getString("usr_phone_type"));
                        cliente.setTelefone(new Telefone(number, ddd, tipo));

                        cache.put(userId, cliente);
                    }

                    // cartão
                    Long cardId = rs.getObject("car_id", Long.class);
                    if (cardId != null) {
                        Cartao cartao = new Cartao();
                        cartao.setId(cardId.intValue());
                        cartao.setPrincipal(rs.getBoolean("car_principal"));
                        cartao.setNumero(rs.getString("car_number"));
                        cartao.setTitular(rs.getString("car_holder"));
                        cartao.setBandeira(Bandeira.from(rs.getString("car_payment_type")));
                        cliente.addCartao(cartao);
                    }

                    // endereço
                    String anaName = rs.getString("ana_name");
                    if (anaName != null) {
                        Endereco endereco = new Endereco();
                        endereco.setApelido(anaName);
                        endereco.setNumero(rs.getString("add_number"));
                        endereco.setObservacoes(rs.getString("add_complement"));
                        endereco.setResidencia(TResidencia.from(rs.getString("rty_residence_type")));
                        endereco.setCidade(new Cidade(rs.getString("cit_city")));
                        endereco.setEstado(new Estado(rs.getString("sta_state"), rs.getString("sta_uf")));
                        endereco.setPais(new Pais(rs.getString("cou_country")));
                        endereco.setCep(rs.getString("str_zip"));
                        endereco.settLogradouro(TLogradouro.from(rs.getString("sty_street_type")));
                        endereco.setLogradouro(new Logradouro(rs.getString("sty_street")));
                        endereco.setBairro("Bairro " + rs.getString("str_neighborhood"));
                        endereco.setTipoEndereco(TEndereco.from(rs.getString("uad_type")));

                        cliente.addEndereco(endereco);
                    }
                }
            }

            return cache.values().stream().findFirst().orElse(null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Cliente> findByFilters(Map<String,String> filters) throws SQLException {
        List<Cliente> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT\n" +
                        "  u.usr_id,\n" +
                        "  u.usr_code,\n" +
                        "  u.usr_name,\n" +
                        "  u.usr_email,\n" +
                        "  u.usr_active,\n" +
                        "  u.usr_birthday,\n" +
                        "  u.usr_cpf,\n" +
                        "  u.usr_phone_type,\n" +
                        "  u.usr_phone_ddd,\n" +
                        "  u.usr_phone_number,\n" +
                        "  u.usr_ranking,\n" +
                        "  g.gen_name AS genero,\n" +
                        "  u.usr_createdAt,\n" +
                        "  u.usr_updatedAt,\n" +
                        "  u.usr_publishedAt\n" +
                        "FROM public.users u\n" +
                        "JOIN public.genders g ON g.gen_id = u.usr_gen_id\n" +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();
        if (filters.containsKey("usr_id")) {
            sql.append(" AND u.usr_id = ?");
            params.add(Integer.parseInt(filters.get("usr_id")));
        }
        if (filters.containsKey("usr_code")) {
            sql.append(" AND u.usr_code = ?");
            params.add(filters.get("usr_code"));
        }
        if (filters.containsKey("usr_name")) {
            sql.append(" AND u.usr_name ILIKE ?");
            params.add("%" + filters.get("usr_name") + "%");
        }
        if (filters.containsKey("usr_email")) {
            sql.append(" AND u.usr_email = ?");
            params.add(filters.get("usr_email"));
        }
        if (filters.containsKey("usr_cpf")) {
            sql.append(" AND u.usr_cpf = ?");
            params.add(filters.get("usr_cpf"));
        }
        if (filters.containsKey("usr_active")) {
            sql.append(" AND u.usr_active = ?");
            params.add(Boolean.parseBoolean(filters.get("usr_active")));
        }
        if (filters.containsKey("usr_ranking")) {
            sql.append(" AND u.usr_ranking = ?");
            params.add(filters.get("usr_ranking"));
        }
        if (filters.containsKey("genero")) {
            sql.append(" AND g.gen_name = ?");
            params.add(filters.get("genero"));
        }

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(   rs.getInt("usr_id"));
                    c.setCodigo(      rs.getString("usr_code"));
                    c.setNome(        rs.getString("usr_name"));
                    c.setEmail(       rs.getString("usr_email"));
                    c.setAtivo(       rs.getBoolean("usr_active"));
                    c.setDataNascimento(rs.getDate("usr_birthday"));
                    c.setCpf(         rs.getString("usr_cpf"));

                    TTelefone tipo = TTelefone.valueOf(rs.getString("usr_phone_type"));
                    c.setTelefone(new Telefone(
                            rs.getString("usr_phone_ddd"),
                            rs.getString("usr_phone_number"),
                            tipo
                    ));

                    c.setRank(        rs.getString("usr_ranking"));
                    c.setGenero(     Genero.valueOf(rs.getString("genero")));

                    lista.add(c);
                }
            }
        }

        return lista;
    }




}