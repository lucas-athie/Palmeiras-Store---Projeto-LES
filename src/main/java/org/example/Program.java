package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import org.example.db.DB;
import org.example.controller.CntrlCliente;
import org.example.controller.CntrlCartao;
import org.example.controller.CntrlEndereco;
import org.example.service.strategy.CriptoStrategy;
import org.example.service.strategy.BCryptCriptoStrategy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Program {
    private static final Logger LOGGER = Logger.getLogger(Program.class.getName());

    public static void main(String[] args) {
        int port = Integer.parseInt(
                System.getProperty("SERVER_PORT",
                        System.getenv().getOrDefault("SERVER_PORT", "8000")
                )
        );
        int poolSize = Integer.parseInt(
                System.getProperty("THREAD_POOL_SIZE",
                        System.getenv().getOrDefault("THREAD_POOL_SIZE", "10")
                )
        );

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.setExecutor(Executors.newFixedThreadPool(poolSize));

            CriptoStrategy cripto = new BCryptCriptoStrategy(12);

            server.createContext("/clientes",
                    new JsonFilter(new CntrlCliente(cripto)));
            server.createContext("/cartoes",
                    new JsonFilter(new CntrlCartao()));
            server.createContext("/enderecos",
                    new JsonFilter(new CntrlEndereco()));

            server.createContext("/", new JsonFilter(exchange -> {
                exchange.sendResponseHeaders(404, 0);
                exchange.getResponseBody()
                        .write("{\"error\":\"Recurso não encontrado\"}".getBytes());
                exchange.close();
            }));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Iniciando shutdown do servidor...");
                server.stop(1);
                DB.closeConnection();  // sem argumentos
                LOGGER.info("Servidor parado.");
            }));

            server.start();
            LOGGER.info("Servidor iniciado em http://localhost:" + port);

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao iniciar servidor", ex);
        }
    }

    static class JsonFilter implements HttpHandler {
        private final HttpHandler next;

        JsonFilter(HttpHandler next) {
            this.next = next;
        }

        @Override
        public void handle(com.sun.net.httpserver.HttpExchange exchange)
                throws IOException {
            exchange.getResponseHeaders().add(
                    "Content-Type", "application/json; charset=UTF-8"
            );
            exchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin", "*"
            );
            try {
                next.handle(exchange);
            } catch (Exception e) {
                String err = String.format("{\"error\":\"%s\"}", e.getMessage());
                exchange.sendResponseHeaders(500, err.getBytes().length);
                exchange.getResponseBody().write(err.getBytes());
                LOGGER.log(Level.SEVERE, "Erro no handler", e);
            } finally {
                exchange.close();
            }
        }
    }
}