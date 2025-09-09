package org.example.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class JsonParserUtil {

    private static final Gson gson = new Gson();


    public static <T> T parse(InputStream in, Class<T> clazz) throws IOException {
        String json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException ex) {
            throw new IOException("JSON inválido: " + ex.getMessage(), ex);
        }
    }


    public static void writeJson(HttpExchange exchange, int statusCode, Object obj) throws IOException {
        String json = gson.toJson(obj);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        var headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Access-Control-Allow-Origin", "*");

        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        var error = Collections.singletonMap("error", message);
        writeJson(exchange, statusCode, error);
    }
}