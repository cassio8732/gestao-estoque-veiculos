package com.estoque.veiculos.web.dto;

import com.estoque.veiculos.domain.StatusDisponibilidade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Cadastro de veículo: marca (ex.: Toyota) e tipo de modelo/carroceria (ex.: SUV)")
public record VeiculoRequest(
        @Schema(description = "Marca do veículo", example = "Toyota")
                @NotBlank(message = "O nome da marca é obrigatório")
                @Size(max = 120, message = "A marca deve ter no máximo 120 caracteres")
                String marca,
        @Schema(description = "Tipo de carroceria", example = "SUV", allowableValues = {"SUV", "Sedan", "Hatch", "Picape"})
                @NotBlank(message = "O tipo de modelo é obrigatório")
                @Size(max = 60, message = "O tipo de modelo deve ter no máximo 60 caracteres")
                String modelo,
        @Schema(example = "2023")
                @NotNull(message = "O ano é obrigatório")
                @Min(value = 1900, message = "O ano deve ser no mínimo 1900")
                @Max(value = 2100, message = "O ano deve ser no máximo 2100")
                Integer ano,
        @Schema(example = "Prata")
                @NotBlank(message = "A cor é obrigatória")
                @Size(max = 60, message = "A cor deve ter no máximo 60 caracteres")
                String cor,
        @Schema(example = "95000.00")
                @NotNull(message = "O preço é obrigatório")
                @Positive(message = "O preço deve ser positivo")
                BigDecimal preco,
        @Schema(example = "15000")
                @NotNull(message = "A quilometragem é obrigatória")
                @PositiveOrZero(message = "A quilometragem não pode ser negativa")
                @Max(value = 9_999_999, message = "A quilometragem informada é inválida")
                Integer quilometragem,
        @Schema(example = "DISPONIVEL") @NotNull(message = "O status é obrigatório") StatusDisponibilidade status) {}
