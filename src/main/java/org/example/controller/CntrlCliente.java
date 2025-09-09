package org.example.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.sun.net.httpserver.HttpExchange;
import org.example.http.BaseHandler;
import org.example.model.entities.Bandeira;
import org.example.model.entities.Cartao;
import org.example.model.entities.Cliente;
import org.example.model.entities.Cidade;
import org.example.model.entities.Endereco;
import org.example.model.entities.Estado;
import org.example.model.entities.Logradouro;
import org.example.model.entities.Pais;
import org.example.model.entities.Telefone;
import org.example.model.entities.TEndereco;
import org.example.model.entities.TLogradouro;
import org.example.model.entities.TResidencia;
import org.example.model.entities.TTelefone;
import org.example.model.entities.Genero;
import org.example.service.CartaoService;
import org.example.service.ClienteService;
import org.example.service.EnderecoService;
import org.example.service.strategy.CodigoStrategy;
import org.example.service.strategy.CriptoStrategy;
import org.example.service.strategy.UUIDCodigoStrategy;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CntrlCliente extends BaseHandler {

    private final Gson gson = new GsonBuilder()
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
            .registerTypeAdapter(Logradouro.class,
                    (JsonDeserializer<Logradouro>)(json, type, ctx) -> {
                        var obj = json.getAsJsonObject();
                        return new Logradouro(obj.get("nome").getAsString());
                    }
            )
            .registerTypeAdapter(Cidade.class,
                    (JsonDeserializer<Cidade>)(json, type, ctx) -> {
                        var obj = json.getAsJsonObject();
                        return new Cidade(obj.get("nome").getAsString());
                    }
            )
            .registerTypeAdapter(Estado.class,
                    (JsonDeserializer<Estado>)(json, type, ctx) -> {
                        var obj = json.getAsJsonObject();
                        return new Estado(
                                obj.get("nome").getAsString(),
                                obj.get("sigla").getAsString()
                        );
                    }
            )
            .registerTypeAdapter(Pais.class,
                    (JsonDeserializer<Pais>)(json, type, ctx) -> {
                        var obj = json.getAsJsonObject();
                        return new Pais(obj.get("nome").getAsString());
                    }
            )
            .create();

    private final CriptoStrategy cripto;
    private final CodigoStrategy codigoStrategy;
    private final ClienteService clienteService;
    private final CartaoService cartaoService;
    private final EnderecoService enderecoService;

    public CntrlCliente(CriptoStrategy cripto) {
        this.cripto         = cripto;
        this.codigoStrategy = new UUIDCodigoStrategy();
        this.clienteService = new ClienteService(cripto, codigoStrategy);
        this.cartaoService  = new CartaoService();
        this.enderecoService= new EnderecoService();
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException, SQLException {
        String method = exchange.getRequestMethod().toUpperCase();
        URI uri       = exchange.getRequestURI();
        List<String> parts = getPathSegments(exchange);

        switch (method) {
            case "GET":
                handleGet(exchange, uri, parts);
                break;

            case "POST":
                if (parts.size() == 2
                        && "clientes".equals(parts.get(0))
                        && "completo".equals(parts.get(1))) {
                    postClienteCompleto(exchange);
                } else if (parts.size() == 3
                        && "clientes".equals(parts.get(0))
                        && "enderecos".equals(parts.get(2))) {
                    postEndereco(exchange, parts.get(1));
                } else if (parts.size() == 3
                        && "clientes".equals(parts.get(0))
                        && "cartoes".equals(parts.get(2))) {
                    postCartao(exchange, parts.get(1));
                } else if (parts.size() == 1
                        && "clientes".equals(parts.get(0))) {
                    postCliente(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1);
                }
                break;

            case "PUT":
                handlePut(exchange, parts);
                break;

            case "DELETE":
                handleDelete(exchange, uri, parts);
                break;

            default:
                exchange.getResponseHeaders()
                        .set("Allow", "GET,POST,PUT,DELETE,OPTIONS");
                exchange.sendResponseHeaders(405, -1);
        }
    }

    private void postClienteCompleto(HttpExchange exchange)
            throws IOException, SQLException {
        Cliente clienteCompleto = gson.fromJson(readBody(exchange), Cliente.class);
        clienteService.cadastrar(clienteCompleto);
        Integer clienteId = clienteCompleto.getIdCliente();

        for (Endereco endereco : clienteCompleto.getEnderecos()) {
            enderecoService.criar(endereco, clienteId);
        }
        for (Cartao cartao : clienteCompleto.getCartoes()) {
            cartaoService.criar(cartao, clienteId);
        }
        writeJson(exchange, 201, Map.of("status", "criado", "id", clienteId));
    }

    private void postCliente(HttpExchange exchange)
            throws IOException, SQLException {
        Cliente novo = gson.fromJson(readBody(exchange), Cliente.class);
        clienteService.cadastrar(novo);
        writeJson(exchange, 201, Map.of("status", "criado"));
    }

    private void postCartao(HttpExchange exchange, String rawClienteId)
            throws IOException, SQLException {
        Integer clienteId = parseIntOr400(rawClienteId, exchange);
        if (clienteId == null) return;
        Cartao cartao = gson.fromJson(readBody(exchange), Cartao.class);
        cartaoService.criar(cartao, clienteId);
        writeJson(exchange, 201, Map.of("status", "criado"));
    }

    private void postEndereco(HttpExchange exchange, String rawClienteId)
            throws IOException, SQLException {
        Integer clienteId = parseIntOr400(rawClienteId, exchange);
        if (clienteId == null) return;
        Endereco endereco = gson.fromJson(readBody(exchange), Endereco.class);
        enderecoService.criar(endereco, clienteId);
        writeJson(exchange, 201, Map.of("status", "criado"));
    }


    private void handleGet(HttpExchange exchange, URI uri, List<String> parts)
            throws IOException, SQLException {

        // GET /clientes?usr_id=123 OR /clientes?search=termo OR /clientes
        if (parts.size() == 1) {
            Map<String, String> qs = splitQuery(uri);

            if (qs.containsKey("usr_id")) {
                Integer id = parseIntOr400(qs.get("usr_id"), exchange);
                if (id == null) return;
                Cliente c = clienteService.buscarPorId(id);
                if (c == null) {
                    exchange.sendResponseHeaders(404, -1);
                } else {
                    writeJson(exchange, 200, c);
                }
                return;
            }

            String termo = qs.getOrDefault("search", "").trim();
            List<Cliente> lista = termo.isEmpty()
                    ? clienteService.listarTodos()
                    : clienteService.listarPorPesquisa(termo);
            writeJson(exchange, 200, lista);
            return;
        }

        if (parts.size() == 2) {
            Integer id = parseIntOr400(parts.get(1), exchange);
            if (id == null) return;
            Cliente c = clienteService.buscarPorId(id);
            if (c == null) {
                exchange.sendResponseHeaders(404, -1);
            } else {
                writeJson(exchange, 200, c);
            }
            return;
        }

        exchange.sendResponseHeaders(404, -1);
    }


    private void handlePut(HttpExchange exchange, List<String> parts)
            throws IOException, SQLException {
        if (parts.size() != 2) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }
        Integer id = parseIntOr400(parts.get(1), exchange);
        if (id == null) return;
        Cliente upd = gson.fromJson(readBody(exchange), Cliente.class);
        upd.setIdCliente(id);
        clienteService.atualizar(upd);
        writeJson(exchange, 200, Map.of("status", "atualizado"));
    }


    private void handleDelete(HttpExchange exchange, URI uri, List<String> parts)
            throws IOException, SQLException {
        if (parts.size() == 4
                && "clientes".equals(parts.get(0))
                && "enderecos".equals(parts.get(2))) {
            Integer clienteId  = parseIntOr400(parts.get(1), exchange);
            Integer enderecoId = parseIntOr400(parts.get(3), exchange);
            if (clienteId == null || enderecoId == null) return;
            enderecoService.excluir(enderecoId, clienteId);
            writeJson(exchange, 200, Map.of("status", "removido"));
            return;
        }

        if (parts.size() == 4
                && "clientes".equals(parts.get(0))
                && "cartoes".equals(parts.get(2))) {
            Integer clienteId = parseIntOr400(parts.get(1), exchange);
            Integer cartaoId  = parseIntOr400(parts.get(3), exchange);
            if (clienteId == null || cartaoId == null) return;
            cartaoService.excluir(cartaoId, clienteId);
            writeJson(exchange, 200, Map.of("status", "removido"));
            return;
        }

        if (parts.size() == 2
                && "clientes".equals(parts.get(0))) {
            Integer id = parseIntOr400(parts.get(1), exchange);
            if (id == null) return;
            clienteService.deletar(id);
            writeJson(exchange, 200, Map.of("status", "removido"));
            return;
        }

        exchange.sendResponseHeaders(404, -1);
    }
}
