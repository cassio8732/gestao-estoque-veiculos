package com.estoque.veiculos.web.dto;

import com.estoque.veiculos.domain.StatusDisponibilidade;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record VeiculoAtualizacaoRequest(
        @Positive(message = "O preço deve ser positivo") BigDecimal preco,
        @PositiveOrZero(message = "A quilometragem não pode ser negativa") Integer quilometragem,
        StatusDisponibilidade status) {}
