package com.estoque.veiculos.config;

import com.estoque.veiculos.domain.Marca;
import com.estoque.veiculos.domain.Modelo;
import com.estoque.veiculos.domain.StatusDisponibilidade;
import com.estoque.veiculos.domain.Veiculo;
import com.estoque.veiculos.repository.MarcaRepository;
import com.estoque.veiculos.repository.ModeloRepository;
import com.estoque.veiculos.repository.VeiculoRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DadosDemonstracaoLoader implements CommandLineRunner {

    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final VeiculoRepository veiculoRepository;

    @Override
    public void run(String... args) {
        if (veiculoRepository.count() > 0) {
            return;
        }
        log.info("Carregando veículos de demonstração...");
        Marca fiat = marcaRepository.save(Marca.builder().nome("Fiat").build());
        Marca vw = marcaRepository.save(Marca.builder().nome("Volkswagen").build());
        Marca toyota = marcaRepository.save(Marca.builder().nome("Toyota").build());

        Modelo hatch = modeloRepository.findByNomeIgnoreCase("Hatch").orElseThrow();
        Modelo sedan = modeloRepository.findByNomeIgnoreCase("Sedan").orElseThrow();
        Modelo suv = modeloRepository.findByNomeIgnoreCase("SUV").orElseThrow();
        Modelo picape = modeloRepository.findByNomeIgnoreCase("Picape").orElseThrow();

        veiculoRepository.saveAll(List.of(
                veiculo(fiat, hatch, 2023, "Branco", "68500.00", 15000, StatusDisponibilidade.DISPONIVEL),
                veiculo(fiat, hatch, 2022, "Vermelho", "58900.00", 32000, StatusDisponibilidade.DISPONIVEL),
                veiculo(vw, hatch, 2024, "Cinza", "112000.00", 5000, StatusDisponibilidade.DISPONIVEL),
                veiculo(vw, sedan, 2021, "Preto", "79900.00", 48000, StatusDisponibilidade.VENDIDO),
                veiculo(toyota, sedan, 2023, "Prata", "145000.00", 22000, StatusDisponibilidade.DISPONIVEL),
                veiculo(toyota, sedan, 2020, "Branco", "108000.00", 61000, StatusDisponibilidade.DESCONTINUADO),
                veiculo(toyota, picape, 2024, "Preto", "289000.00", 8000, StatusDisponibilidade.DISPONIVEL),
                veiculo(toyota, suv, 2022, "Azul", "198000.00", 35000, StatusDisponibilidade.DISPONIVEL)));
    }

    private static Veiculo veiculo(
            Marca marca,
            Modelo modelo,
            int ano,
            String cor,
            String preco,
            int km,
            StatusDisponibilidade status) {
        return Veiculo.builder()
                .marca(marca)
                .modelo(modelo)
                .ano(ano)
                .cor(cor)
                .preco(new BigDecimal(preco))
                .quilometragem(km)
                .status(status)
                .build();
    }
}
