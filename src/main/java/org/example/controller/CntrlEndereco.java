package org.example.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.example.db.DB;
import org.example.http.BaseHandler;
import org.example.model.dao.EnderecoDao;
import org.example.model.dao.DaoFactory;
import org.example.model.entities.Endereco;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CntrlEndereco extends BaseHandler {
    private final Gson gson = new Gson();

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        Map<String,String> params = splitQuery(uri);

        String sCli = params.get("clienteId");
        if (sCli == null) {
            writeJson(exchange, 400, "{\"error\":\"clienteId é obrigatório\"}");
            return;
        }
        int clienteId = Integer.parseInt(sCli);

        try (Connection conn = DB.getConnection()) {
            EnderecoDao dao = DaoFactory.createEnderecoDao(conn);

            switch (method.toUpperCase()) {
                case "GET":
                    List<Endereco> lista = dao.findEnderecoByClientId(clienteId);
                    writeJson(exchange, 200, gson.toJson(lista));
                    break;

                case "POST":
                    Endereco novo = gson.fromJson(readBody(exchange), Endereco.class);
                    dao.inserir(novo, clienteId);
                    writeJson(exchange, 201, "{\"status\":\"criado\"}");
                    break;

                case "DELETE":
                    String sEnd = params.get("enderecoId");
                    if (sEnd == null) {
                        writeJson(exchange, 400, "{\"error\":\"enderecoId é obrigatório para DELETE\"}");
                        break;
                    }
                    int enderecoId = Integer.parseInt(sEnd);
                    dao.deleteById(enderecoId, clienteId);
                    writeJson(exchange, 200, "{\"status\":\"removido\"}");
                    break;

                default:
                    exchange.getResponseHeaders().add("Allow", "GET,POST,DELETE,OPTIONS");
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