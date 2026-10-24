package com.estoque.veiculos.config;

import com.estoque.veiculos.domain.Modelo;
import com.estoque.veiculos.repository.ModeloRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@RequiredArgsConstructor
@Slf4j
public class ModelosIniciaisLoader implements CommandLineRunner {

    public static final List<String> TIPOS_PADRAO = List.of("SUV", "Sedan", "Hatch", "Picape");

    private final ModeloRepository modeloRepository;

    @Override
    public void run(String... args) {
        int criados = 0;
        for (String nome : TIPOS_PADRAO) {
            if (!modeloRepository.existsByNomeIgnoreCase(nome)) {
                modeloRepository.save(Modelo.builder().nome(nome).build());
                criados++;
            }
        }
        if (criados > 0) {
            log.info("Tipos de modelo padrão cadastrados: {}", TIPOS_PADRAO);
        }
    }
}
