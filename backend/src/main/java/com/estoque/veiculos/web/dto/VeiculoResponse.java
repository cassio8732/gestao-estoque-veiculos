package com.estoque.veiculos.web.dto;

import com.estoque.veiculos.domain.StatusDisponibilidade;
import java.math.BigDecimal;

public record VeiculoResponse(
        Long id,
        Long modeloId,
        String modeloNome,
        Long marcaId,
        String marcaNome,
        Integer ano,
        String cor,
        BigDecimal preco,
        Integer quilometragem,
        StatusDisponibilidade status) {}
