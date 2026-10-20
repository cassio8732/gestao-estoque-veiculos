package com.estoque.veiculos.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MarcaRequest(
        @NotBlank(message = "O nome da marca é obrigatório")
                @Size(max = 120, message = "O nome da marca deve ter no máximo 120 caracteres")
                String nome) {}
