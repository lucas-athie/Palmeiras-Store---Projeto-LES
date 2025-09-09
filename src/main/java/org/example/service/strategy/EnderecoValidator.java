package org.example.service.strategy;

import org.example.model.entities.Endereco;
import org.example.model.entities.Logradouro;
import org.example.model.entities.Cidade;
import org.example.model.entities.Estado;
import org.example.model.entities.Pais;

public class EnderecoValidator implements ValidationStrategy<Endereco> {

    @Override
    public ValidationResult validate(Endereco e) {
        ValidationResult result = new ValidationResult();

        if (e == null) {
            result.addError("Endereço não pode ser nulo");
            return result;
        }

        if (e.getTipoEndereco() == null) {
            result.addError("Tipo de endereço é obrigatório");
        }

        String cep = e.getCep();
        if (isBlank(cep)) {
            result.addError("CEP é obrigatório");
        } else if (cep.length() != 8 || !cep.chars().allMatch(Character::isDigit)) {
            result.addError("CEP deve ter exatamente 8 dígitos numéricos");
        }

        if (isBlank(e.getNumero())) {
            result.addError("Número é obrigatório");
        }

        if (isBlank(e.getBairro())) {
            result.addError("Bairro é obrigatório");
        }

        if (e.getTipoResidencia() == null) {
            result.addError("Tipo de residência é obrigatório");
        }

        if (e.getTipoLogradouro() == null) {
            result.addError("Tipo de logradouro é obrigatório");
        }

        Logradouro log = e.getLogradouro();
        if (log == null || isBlank(log.getNome())) {
            result.addError("Logradouro é obrigatório");
        }

        Cidade city = e.getCidade();
        if (city == null || isBlank(city.getNome())) {
            result.addError("Cidade é obrigatória");
        }

        Estado state = e.getEstado();
        if (state == null) {
            result.addError("Estado é obrigatório");
        } else {
            if (isBlank(state.getSigla())) {
                result.addError("Sigla do estado é obrigatória");
            }
        }

        Pais country = e.getPais();
        if (country == null || isBlank(country.getNome())) {
            result.addError("País é obrigatório");
        }

        return result;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}