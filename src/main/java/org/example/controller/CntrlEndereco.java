package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.sun.net.httpserver.HttpExchange;
import org.example.db.DB;
import org.example.http.BaseHandler;
import org.example.model.dao.DaoFactory;
import org.example.model.dao.EnderecoDao;
import org.example.model.entities.Endereco;
import org.example.model.entities.TEndereco;
import org.example.service.strategy.EnderecoValidator;
import org.example.service.strategy.EnderecosValidator;
import org.example.service.strategy.ValidationResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CntrlEndereco extends BaseHandler {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    TEndereco.class,
                    (JsonDeserializer<TEndereco>) (json, type, ctx) ->
                            TEndereco.from(json.getAsString())
            )
            .create();

    private final EnderecoValidator  fieldValidator = new EnderecoValidator();
    private final EnderecosValidator listValidator  = new EnderecosValidator();

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {

        var headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin",  "*");
        headers.add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String method = exchange.getRequestMethod().toUpperCase();
        List<String> parts = getPathSegments(exchange);

        if (parts.size() < 3
                || !"clientes".equals(parts.get(0))
                || !"enderecos".equals(parts.get(2))) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        Integer clienteId = parseIntOr400(parts.get(1), exchange);
        if (clienteId == null) return;

        try (Connection conn = DB.getConnection()) {
            EnderecoDao dao = DaoFactory.createEnderecoDao(conn);

            if ("GET".equals(method) && parts.size() == 3) {
                List<Endereco> lista = dao.findByClienteId(clienteId);
                writeJson(exchange, 200, lista);
                return;
            }

            if ("GET".equals(method) && parts.size() == 4) {
                Integer enderecoId = parseIntOr400(parts.get(3), exchange);
                if (enderecoId == null) return;

                Endereco found = dao.findByClienteId(clienteId).stream()
                        .filter(e -> e.getIdEndereco() == enderecoId)
                        .findFirst()
                        .orElse(null);

                if (found == null) {
                    exchange.sendResponseHeaders(404, -1);
                } else {
                    writeJson(exchange, 200, found);
                }
                return;
            }

            if ("POST".equals(method) && parts.size() == 3) {
                Endereco novo = gson.fromJson(readBody(exchange), Endereco.class);

                ValidationResult vrField = fieldValidator.validate(novo);
                vrField.assertValid();

                if (novo.getCidade() == null ||
                        novo.getCidade().getNome() == null ||
                        novo.getCidade().getNome().isBlank()) {
                    writeJson(exchange, 400,
                            Map.of("error","Campo 'cidade.nome' é obrigatório"));
                    return;
                }
                if (novo.getEstado() == null ||
                        novo.getEstado().getNome() == null ||
                        novo.getEstado().getSigla() == null ||
                        novo.getEstado().getSigla().isBlank()) {
                    writeJson(exchange, 400,
                            Map.of("error","Campos 'estado.nome' e 'estado.uf' são obrigatórios"));
                    return;
                }
                if (novo.getPais() == null ||
                        novo.getPais().getNome() == null ||
                        novo.getPais().getNome().isBlank()) {
                    writeJson(exchange, 400,
                            Map.of("error","Campo 'pais.nome' é obrigatório"));
                    return;
                }
                if (novo.getLogradouro() == null ||
                        novo.getLogradouro().getNome() == null ||
                        novo.getLogradouro().getNome().isBlank()) {
                    writeJson(exchange, 400,
                            Map.of("error","Campo 'logradouro.nome' é obrigatório"));
                    return;
                }

                dao.insert(clienteId, novo);
                writeJson(exchange, 201, Map.of("status","criado"));
                return;
            }

            if ("DELETE".equals(method) && parts.size() == 4) {
                Integer enderecoId = parseIntOr400(parts.get(3), exchange);
                if (enderecoId == null) return;

                List<Endereco> restantes = dao.findByClienteId(clienteId).stream()
                        .filter(e -> e.getIdEndereco() != enderecoId)
                        .collect(Collectors.toList());
                ValidationResult vrList = listValidator.validate(restantes);
                vrList.assertValid();

                dao.deleteById(clienteId, enderecoId);
                writeJson(exchange, 200, Map.of("status","removido"));
                return;
            }

            headers.set("Allow","GET,POST,DELETE,OPTIONS");
            exchange.sendResponseHeaders(405, -1);

        } catch (SQLException e) {
            writeJson(exchange, 500, Map.of("error", e.getMessage()));
        }
    }
}