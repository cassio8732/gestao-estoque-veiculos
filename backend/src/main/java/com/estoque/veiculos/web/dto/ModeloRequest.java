package com.estoque.veiculos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Tipo de carroceria do veículo (SUV, Sedan, Hatch, Picape, etc.)")
public record ModeloRequest(
        @Schema(description = "Nome do tipo", example = "SUV")
                @NotBlank(message = "O nome do tipo de modelo é obrigatório")
                @Size(max = 60, message = "O nome deve ter no máximo 60 caracteres")
                String nome) {}
