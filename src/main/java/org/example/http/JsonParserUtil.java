package org.example.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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

    public static void writeJson(OutputStream out, int statusCode, Object obj,
                                 com.sun.net.httpserver.HttpExchange ex) throws IOException {
        String json = gson.toJson(obj);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}