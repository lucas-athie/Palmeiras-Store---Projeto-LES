package org.example.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.model.entities.Bandeira;
import org.example.model.entities.Genero;
import org.example.model.entities.TEndereco;
import org.example.model.entities.TLogradouro;
import org.example.model.entities.TResidencia;
import org.example.model.entities.TTelefone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseHandler implements HttpHandler {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    TResidencia.class,
                    (JsonDeserializer<TResidencia>) (json, type, ctx) ->
                            TResidencia.from(json.getAsString())
            )
            .registerTypeAdapter(
                    TLogradouro.class,
                    (JsonDeserializer<TLogradouro>) (json, type, ctx) ->
                            TLogradouro.from(json.getAsString())
            )
            .registerTypeAdapter(
                    TEndereco.class,
                    (JsonDeserializer<TEndereco>) (json, type, ctx) ->
                            TEndereco.from(json.getAsString())
            )
            .registerTypeAdapter(
                    Genero.class,
                    (JsonDeserializer<Genero>) (json, type, ctx) ->
                            Genero.from(json.getAsString())
            )
            .registerTypeAdapter(
                    Bandeira.class,
                    (JsonDeserializer<Bandeira>) (json, type, ctx) ->
                            Bandeira.from(json.getAsString())
            )
            .registerTypeAdapter(
                    TTelefone.class,
                    (JsonDeserializer<TTelefone>) (json, type, ctx) ->
                            TTelefone.from(json.getAsString())
            )
            .create();

    private static final String CORS_ORIGIN  = "*";
    private static final String CORS_METHODS = "GET,POST,PUT,DELETE,OPTIONS";
    private static final String CORS_HEADERS = "Content-Type";

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        try {
            addCORSHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            handleRequest(exchange);
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception e) {
            sendError(exchange, 500, e.getMessage());
        }
    }

    /**
     * Implementado pelo controlador (ex: CntrlCliente) para tratar GET/POST/PUT/DELETE.
     */
    protected abstract void handleRequest(HttpExchange exchange) throws IOException, SQLException;

    protected void addCORSHeaders(HttpExchange exchange) {
        var headers = exchange.getResponseHeaders();
        headers.set("Access-Control-Allow-Origin",  CORS_ORIGIN);
        headers.set("Access-Control-Allow-Methods", CORS_METHODS);
        headers.set("Access-Control-Allow-Headers", CORS_HEADERS);
    }

    protected String readBody(HttpExchange exchange) throws IOException {
        try (InputStream in = exchange.getRequestBody()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    protected <T> T readJson(HttpExchange exchange, Class<T> clazz) throws IOException {
        return gson.fromJson(readBody(exchange), clazz);
    }

    protected void writeJson(HttpExchange exchange, int statusCode, Object obj)
            throws IOException {
        String json  = gson.toJson(obj);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders()
                .set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    protected void sendError(HttpExchange exchange, int statusCode, String message)
            throws IOException {
        Map<String,String> error = Collections.singletonMap("error", message);
        writeJson(exchange, statusCode, error);
    }

    protected Map<String,String> splitQuery(URI uri) {
        Map<String,String> map = new LinkedHashMap<>();
        String raw = uri.getQuery();
        if (raw == null || raw.isEmpty()) return map;
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

    protected List<String> getPathSegments(HttpExchange exchange) {
        return Arrays.stream(
                        exchange.getRequestURI().getPath().split("/")
                )
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    protected Integer extractId(HttpExchange exchange) throws IOException {
        List<String> parts = getPathSegments(exchange);
        if (parts.size() >= 2) {
            return parseIntOr400(parts.get(1), exchange);
        }
        Map<String,String> qs = splitQuery(exchange.getRequestURI());
        if (qs.containsKey("usr_id")) {
            return parseIntOr400(qs.get("usr_id"), exchange);
        }
        sendError(exchange, 400, "ID não especificado");
        return null;
    }

    protected Integer parseIntOr400(String raw, HttpExchange exchange)
            throws IOException {
        try {
            return Integer.valueOf(raw);
        } catch (NumberFormatException e) {
            sendError(exchange, 400, "ID inválido: " + raw);
            return null;
        }
    }
}
