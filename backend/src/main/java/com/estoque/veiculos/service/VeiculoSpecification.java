package com.estoque.veiculos.service;

import com.estoque.veiculos.domain.StatusDisponibilidade;
import com.estoque.veiculos.domain.Veiculo;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public final class VeiculoSpecification {

    private VeiculoSpecification() {}

    public static Specification<Veiculo> comFiltros(
            String marca,
            String modelo,
            Integer anoMin,
            Integer anoMax,
            BigDecimal precoMin,
            BigDecimal precoMax,
            StatusDisponibilidade status) {
        return Specification.allOf(
                comMarca(marca),
                comModelo(modelo),
                comAnoMin(anoMin),
                comAnoMax(anoMax),
                comPrecoMin(precoMin),
                comPrecoMax(precoMax),
                comStatus(status));
    }

    private static Specification<Veiculo> comMarca(String marca) {
        return (root, query, cb) -> {
            if (marca == null || marca.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("marca").get("nome")), "%" + marca.toLowerCase() + "%");
        };
    }

    private static Specification<Veiculo> comModelo(String modelo) {
        return (root, query, cb) -> {
            if (modelo == null || modelo.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("modelo").get("nome")), "%" + modelo.toLowerCase() + "%");
        };
    }

    private static Specification<Veiculo> comAnoMin(Integer anoMin) {
        return (root, query, cb) ->
                anoMin == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("ano"), anoMin);
    }

    private static Specification<Veiculo> comAnoMax(Integer anoMax) {
        return (root, query, cb) -> anoMax == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("ano"), anoMax);
    }

    private static Specification<Veiculo> comPrecoMin(BigDecimal precoMin) {
        return (root, query, cb) ->
                precoMin == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("preco"), precoMin);
    }

    private static Specification<Veiculo> comPrecoMax(BigDecimal precoMax) {
        return (root, query, cb) ->
                precoMax == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("preco"), precoMax);
    }

    private static Specification<Veiculo> comStatus(StatusDisponibilidade status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }
}
