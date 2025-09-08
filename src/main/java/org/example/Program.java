package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.controller.CntrlCartao;
import org.example.controller.CntrlCliente;
import org.example.controller.CntrlEndereco;

import java.net.InetSocketAddress;

public class Program {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/clientes", new CntrlCliente());
        server.createContext("/enderecos", new CntrlEndereco());
        server.createContext("/auth",      new CntrlAuth());
        server.createContext("/cartoes",   new CntrlCartao());   // ← aqui

        server.setExecutor(null);
        server.start();
        System.out.println("Server rodando em http://localhost:8000");
    }
}