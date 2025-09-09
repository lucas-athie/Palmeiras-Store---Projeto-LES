package org.example.service.strategy;

import org.example.model.entities.Endereco;
import org.example.model.entities.TEndereco;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class EnderecosValidator implements ValidationStrategy<List<Endereco>> {

    private static final Set<TEndereco> REQUIRED =
            Set.of(TEndereco.COBRANCA, TEndereco.ENTREGA);

    @Override
    public ValidationResult validate(List<Endereco> enderecos) {
        ValidationResult result = new ValidationResult();

        if (enderecos == null || enderecos.isEmpty()) {
            result.addError(
                    "É necessário informar ao menos um endereço de cobrança e um de entrega"
            );
            return result;
        }

        Set<TEndereco> tiposPresentes = enderecos.stream()
                .map(Endereco::getTipoEndereco)
                .collect(Collectors.toSet());

        for (TEndereco req : REQUIRED) {
            if (!tiposPresentes.contains(req)) {
                switch (req) {
                    case ENTREGA ->
                            result.addError("Falta pelo menos um endereço de entrega");
                    case COBRANCA ->
                            result.addError("Falta pelo menos um endereço de cobrança");
                }
            }
        }

        return result;
    }
}