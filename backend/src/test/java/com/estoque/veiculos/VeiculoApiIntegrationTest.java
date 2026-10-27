package com.estoque.veiculos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.estoque.veiculos.domain.StatusDisponibilidade;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VeiculoApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void seed() throws Exception {
        mockMvc.perform(post("/api/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("nome", "Honda"))))
                .andExpect(status().isCreated());
    }

    @Test
    void fluxoCrudEFiltro() throws Exception {
        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "marca", "Honda",
                                "modelo", "Sedan",
                                "ano", 2022,
                                "cor", "Azul",
                                "preco", new BigDecimal("95000.00"),
                                "quilometragem", 12000,
                                "status", StatusDisponibilidade.DISPONIVEL))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.marcaNome").value("Honda"))
                .andExpect(jsonPath("$.modeloNome").value("Sedan"));

        MvcResult listagem = mockMvc.perform(get("/api/veiculos")
                        .param("marca", "Honda")
                        .param("modelo", "Sedan")
                        .param("status", "DISPONIVEL")
                        .param("precoMax", "100000"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(listagem.getResponse().getContentAsString()).contains("Sedan");

        long veiculoId =
                objectMapper.readTree(listagem.getResponse().getContentAsString()).get(0).get("id").asLong();

        mockMvc.perform(patch("/api/veiculos/" + veiculoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("preco", 89900, "status", "VENDIDO"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("VENDIDO"));
    }
}
