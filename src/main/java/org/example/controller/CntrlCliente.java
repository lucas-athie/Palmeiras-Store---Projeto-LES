package org.example.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.example.db.DB;
import org.example.http.BaseHandler;
import org.example.model.dao.ClienteDao;
import org.example.model.dao.DaoFactory;
import org.example.model.entities.Cliente;
import org.example.service.strategy.CriptoStrategy;
import org.example.service.strategy.CriSenha;


import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CntrlCliente extends BaseHandler {
    private final Gson gson = new Gson();
    private final CriptoStrategy cripto = new CriSenha(12);

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String method  = exchange.getRequestMethod();
        URI uri        = exchange.getRequestURI();
        String[] segs  = uri.getPath().split("/");

        try (Connection conn = DB.getConnection()) {
            ClienteDao dao = DaoFactory.createClienteDao(conn);

            if ("GET".equalsIgnoreCase(method)) {
                if (segs.length == 2) {
                    Map<String,String> filtros = splitQuery(uri);
                    List<Cliente> lista = dao.findByFilters(filtros);
                    writeJson(exchange, 200, gson.toJson(lista));

                } else if (segs.length == 3) {
                    int id = Integer.parseInt(segs[2]);
                    Cliente c = dao.findById(id);
                    writeJson(exchange, 200, gson.toJson(c));

                } else {
                    exchange.getResponseHeaders().add("Allow", "GET,POST,PUT,DELETE,OPTIONS");
                    exchange.sendResponseHeaders(404, -1);
                }

            } else if ("POST".equalsIgnoreCase(method)) {
                Cliente novo = gson.fromJson(readBody(exchange), Cliente.class);

                String senhaPura   = novo.getSenha();
                String senhaHash   = cripto.encrypt(senhaPura);
                novo.setSenha(senhaHash);

                dao.insert(novo);
                writeJson(exchange, 201, "{\"status\":\"criado\"}");

            } else if ("PUT".equalsIgnoreCase(method) && segs.length == 3) {
                int id = Integer.parseInt(segs[2]);
                Cliente atual = gson.fromJson(readBody(exchange), Cliente.class);
                atual.setIdCliente(id);
                dao.update(atual);
                writeJson(exchange, 200, "{\"status\":\"atualizado\"}");

            } else if ("DELETE".equalsIgnoreCase(method) && segs.length == 3) {
                int id = Integer.parseInt(segs[2]);
                dao.deleteById(id);
                writeJson(exchange, 200, "{\"status\":\"removido\"}");

            } else {
                exchange.getResponseHeaders().add("Allow", "GET,POST,PUT,DELETE,OPTIONS");
                exchange.sendResponseHeaders(405, -1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            writeJson(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private Map<String,String> splitQuery(URI uri) {
        Map<String,String> map = new HashMap<>();
        String raw = uri.getQuery();
        if (raw == null) return map;

        for (String param : raw.split("&")) {
            String[] kv = param.split("=", 2);
            if (kv.length == 2) {
                String key = kv[0];
                String val = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                map.put(key, val);
            }
        }
        return map;
    }
}