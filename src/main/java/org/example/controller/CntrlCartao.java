package org.example.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.example.db.DB;
import org.example.http.BaseHandler;
import org.example.model.dao.CartaoDao;
import org.example.model.dao.DaoFactory;
import org.example.model.entities.Cartao;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CntrlCartao extends BaseHandler {
    private final Gson gson = new Gson();

    @Override
    protected void handleRequest(HttpExchange ex) throws IOException {
        try {
            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
                ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                ex.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,DELETE,OPTIONS");
                ex.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
                ex.sendResponseHeaders(204, -1);
                return;
            }

            String method = ex.getRequestMethod();
            Map<String, String> query = splitQuery(ex.getRequestURI());
            String cid = query.get("clienteId");
            if (cid == null) {
                writeJson(ex, 400, "{\"error\":\"clienteId is required\"}");
                return;
            }
            int clienteId = Integer.parseInt(cid);

            try (Connection conn = DB.getConnection()) {
                CartaoDao dao = DaoFactory.createCartaoDao(conn);

                switch (method) {
                    case "GET":
                        List<Cartao> lista = dao.findCartaoByClienteId(clienteId);
                        writeJson(ex, 200, gson.toJson(lista));
                        break;

                    case "POST":
                        Cartao novo = gson.fromJson(readBody(ex), Cartao.class);
                        dao.insert(novo, clienteId);
                        writeJson(ex, 201, "{\"status\":\"criado\"}");
                        break;

                    case "DELETE":
                        String tid = query.get("cartaoId");
                        if (tid == null) {
                            writeJson(ex, 400, "{\"error\":\"cartaoId is required for DELETE\"}");
                            break;
                        }
                        int cartaoId = Integer.parseInt(tid);
                        dao.deleteByid(cartaoId);
                        writeJson(ex, 200, "{\"status\":\"removido\"}");
                        break;

                    default:
                        ex.getResponseHeaders().add("Allow", "GET,POST,DELETE,OPTIONS");
                        ex.sendResponseHeaders(405, -1);
                }

            } catch (SQLException sq) {
                errorJson(ex, 500, sq);
            }

        } catch (Exception e) {
            errorJson(ex, 500, e);
        }
    }

    private Map<String,String> splitQuery(URI uri) {
        Map<String,String> map = new HashMap<>();
        String raw = uri.getQuery();
        if (raw == null || raw.isEmpty()) return map;
        for (String kv : raw.split("&")) {
            String[] pair = kv.split("=", 2);
            if (pair.length == 2) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }

    private void errorJson(HttpExchange ex, int status, Exception e) throws IOException {
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        writeJson(ex, status, "{\"error\":\"" + e.getMessage() + "\"}");
    }
}