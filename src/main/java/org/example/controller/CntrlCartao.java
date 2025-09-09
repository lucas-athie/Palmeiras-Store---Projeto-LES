package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import org.example.config.Database;
import org.example.http.BaseHandler;
import org.example.model.dao.CartaoDao;
import org.example.model.dao.DaoFactory;
import org.example.model.entities.Cartao;
import org.example.service.strategy.BandeiraValidator;
import org.example.service.strategy.CartaoValidator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CntrlCartao extends BaseHandler {

    private final Gson gson = new GsonBuilder().create();
    private final CartaoValidator validator = new CartaoValidator();

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String method = exchange.getRequestMethod().toUpperCase();
        List<String> parts = getPathSegments(exchange);

        if (parts.size() < 3
                || !"clientes".equals(parts.get(0))
                || !"cartoes".equals(parts.get(2))) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        Integer clienteId = parseIntOr400(parts.get(1), exchange);
        if (clienteId == null) return;

        try (Connection conn = Database.getConnection()) {
            CartaoDao dao = DaoFactory.createCartaoDao(conn);

            if ("GET".equals(method) && parts.size() == 3) {
                List<Cartao> lista = dao.findByClienteId(clienteId);
                writeJson(exchange, 200, lista);
                return;
            }

            if ("GET".equals(method) && parts.size() == 4) {
                Integer cartaoId = parseIntOr400(parts.get(3), exchange);
                if (cartaoId == null) return;

                List<Cartao> lista = dao.findByClienteId(clienteId);
                Cartao found = lista.stream()
                        .filter(c -> c.getId() == cartaoId)
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
                Cartao novo = gson.fromJson(readBody(exchange), Cartao.class);
                validator.validate(novo).assertValid();

                if (!BandeiraValidator.isValid(novo.getBandeira().name())) {
                    writeJson(exchange, 400,
                            Map.of("error", "Bandeira inválida. Use uma das permitidas.")
                    );
                    return;
                }

                dao.insert(clienteId, novo);
                writeJson(exchange, 201, Map.of("status", "criado"));
                return;
            }

            if ("DELETE".equals(method) && parts.size() == 4) {
                Integer cartaoId = parseIntOr400(parts.get(3), exchange);
                if (cartaoId == null) return;

                dao.deleteById(clienteId, cartaoId);
                writeJson(exchange, 200, Map.of("status", "removido"));
                return;
            }

            if ("OPTIONS".equals(method)) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods",
                        "GET,POST,DELETE,OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers",
                        "Content-Type");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            exchange.getResponseHeaders().set("Allow",
                    "GET,POST,DELETE,OPTIONS");
            exchange.sendResponseHeaders(405, -1);

        } catch (SQLException e) {
            writeJson(exchange, 500, Map.of("error", e.getMessage()));
        }
    }
}