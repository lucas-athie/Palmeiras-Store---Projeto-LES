package org.example.service.strategy;

import org.example.model.entities.Cartao;
import org.example.model.entities.Cliente;
import org.example.model.entities.Endereco;
import org.example.model.entities.TEndereco;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteValidator implements ValidationStrategy<Cliente> {

    private static final Pattern EMAIL =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern CPF =
            Pattern.compile("^\\d{11}$");
    private static final Pattern SENHA_FORTE =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$");

    private final EnderecoValidator enderecoValidator = new EnderecoValidator();
    private final CartaoValidator    cartaoValidator    = new CartaoValidator();

    @Override
    public ValidationResult validate(Cliente cliente) {
        ValidationResult result = new ValidationResult();

        if (cliente == null) {
            result.addError("Cliente não pode ser nulo");
            return result;
        }

        if (isBlank(cliente.getNome())) {
            result.addError("Nome é obrigatório");
        }

        String email = cliente.getEmail();
        if (isBlank(email) || !EMAIL.matcher(email).matches()) {
            result.addError("Email inválido");
        }

        String cpf = cliente.getCpf();
        if (isBlank(cpf) || !CPF.matcher(cpf).matches()) {
            result.addError("CPF deve ter exatamente 11 dígitos numéricos");
        }

        Date nasc = cliente.getDataNascimento();
        if (nasc == null) {
            result.addError("Data de nascimento é obrigatória");
        } else if (nasc.after(new Date())) {
            result.addError("Data de nascimento não pode ser futura");
        }

        String senha = cliente.getSenha();
        if (isBlank(senha) || !SENHA_FORTE.matcher(senha).matches()) {
            result.addError(
                    "Senha deve ter ≥8 caracteres, letras maiúsculas, minúsculas e símbolos"
            );
        }

        if (cliente.getTelefone() == null) {
            result.addError("Telefone é obrigatório");
        }

        if (cliente.getGenero() == null) {
            result.addError("Gênero é obrigatório");
        }

        List<Endereco> enderecos = cliente.getEnderecos();
        if (enderecos == null || enderecos.size() != 2) {
            result.addError(
                    "É necessário informar dois endereços: um de entrega e um de cobrança"
            );
        } else {
            boolean temEntrega  = enderecos.stream()
                    .anyMatch(e -> e.getTipoEndereco() == TEndereco.ENTREGA);
            boolean temCobranca = enderecos.stream()
                    .anyMatch(e -> e.getTipoEndereco() == TEndereco.COBRANCA);

            if (!temEntrega) {
                result.addError("É necessário informar um endereço de entrega");
            }
            if (!temCobranca) {
                result.addError("É necessário informar um endereço de cobrança");
            }

            for (Endereco e : enderecos) {
                result.merge(enderecoValidator.validate(e));
            }
        }

        List<Cartao> cartoes = cliente.getCartoes();
        if (cartoes == null || cartoes.isEmpty()) {
            result.addError("Pelo menos um cartão é obrigatório");
        } else {
            for (Cartao c : cartoes) {
                result.merge(cartaoValidator.validate(c));
            }
        }

        return result;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
