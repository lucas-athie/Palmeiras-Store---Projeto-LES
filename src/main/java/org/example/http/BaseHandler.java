package org.example.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class BaseHandler implements HttpHandler {
    private static final Gson gson = new Gson();

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        try {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleOptions(exchange);
                return;
            }
            handleRequest(exchange);

        } catch (Exception e) {
            sendError(exchange, 500, e.getMessage());
        } finally {
            exchange.close();
        }
    }

    protected void handleOptions(HttpExchange exchange) throws IOException {
        var headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }

    protected String readBody(HttpExchange exchange) throws IOException {
        try (InputStream in = exchange.getRequestBody()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    protected <T> T readJson(HttpExchange exchange, Class<T> clazz) throws IOException {
        String body = readBody(exchange);
        return gson.fromJson(body, clazz);
    }

    protected void writeJson(HttpExchange exchange, int statusCode, Object obj) throws IOException {
        String json = gson.toJson(obj);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        var headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Origin", "*");

        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    protected void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        Map<String,String> err = Map.of("error", message);
        writeJson(exchange, statusCode, err);
    }

    protected Map<String,String> parseQueryParams(URI uri) {
        Map<String,String> map = new HashMap<>();
        String raw = uri.getQuery();
        if (raw == null) return map;

        for (String kv : raw.split("&")) {
            String[] parts = kv.split("=", 2);
            if (parts.length == 2) {
                String key = parts[0];
                String val = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                map.put(key, val);
            }
        }
        return map;
    }

    protected abstract void handleRequest(HttpExchange exchange) throws IOException;
}