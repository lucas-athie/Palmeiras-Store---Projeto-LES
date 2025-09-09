package org.example.service.strategy;

import org.example.model.entities.Cartao;

public class CartaoValidator implements ValidationStrategy<Cartao> {

    @Override
    public ValidationResult validate(Cartao c) {
        ValidationResult result = new ValidationResult();

        if (c == null) {
            result.addError("Cartão não pode ser nulo");
            return result;
        }

        String num = c.getNumero();
        if (isBlank(num)) {
            result.addError("Número do cartão é obrigatório");
        } else if (num.length() < 13 || num.length() > 19) {
            result.addError("Número do cartão deve ter entre 13 e 19 dígitos");
        } else if (!num.chars().allMatch(Character::isDigit)) {
            result.addError("Número do cartão deve conter apenas dígitos");
        }

        if (isBlank(c.getTitular())) {
            result.addError("Nome do titular é obrigatório");
        }

        if (c.getBandeira() == null) {
            result.addError("Bandeira do cartão é obrigatória");
        }

        String cvc = c.getCodigoSeguranca();
        if (isBlank(cvc)) {
            result.addError("Código de segurança é obrigatório");
        } else if (cvc.length() < 3 || cvc.length() > 4) {
            result.addError("Código de segurança deve ter 3 ou 4 dígitos");
        } else if (!cvc.chars().allMatch(Character::isDigit)) {
            result.addError("Código de segurança deve conter apenas dígitos");
        }

        return result;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}