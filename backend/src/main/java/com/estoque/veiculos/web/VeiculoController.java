package com.estoque.veiculos.web;

import com.estoque.veiculos.domain.StatusDisponibilidade;
import com.estoque.veiculos.service.VeiculoService;
import com.estoque.veiculos.web.dto.VeiculoAtualizacaoRequest;
import com.estoque.veiculos.web.dto.VeiculoRequest;
import com.estoque.veiculos.web.dto.VeiculoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
@Tag(name = "Veículos", description = "CRUD e consulta com filtros")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Operation(
            summary = "Listar veículos",
            description =
                    "Retorna todos os veículos ou apenas os que correspondem aos filtros informados (todos opcionais).")
    @GetMapping
    public List<VeiculoResponse> listar(
            @Parameter(
                            name = "marca",
                            in = ParameterIn.QUERY,
                            description = "Filtrar por marca (contém)",
                            example = "Toyota")
                    @RequestParam(required = false)
                    String marca,
            @Parameter(
                            name = "modelo",
                            in = ParameterIn.QUERY,
                            description = "Filtrar por tipo de carroceria",
                            example = "SUV")
                    @RequestParam(required = false)
                    String modelo,
            @Parameter(name = "anoMin", in = ParameterIn.QUERY, description = "Ano mínimo", example = "2020")
                    @RequestParam(required = false)
                    Integer anoMin,
            @Parameter(name = "anoMax", in = ParameterIn.QUERY, description = "Ano máximo", example = "2024")
                    @RequestParam(required = false)
                    Integer anoMax,
            @Parameter(
                            name = "precoMin",
                            in = ParameterIn.QUERY,
                            description = "Preço mínimo",
                            example = "50000")
                    @RequestParam(required = false)
                    BigDecimal precoMin,
            @Parameter(
                            name = "precoMax",
                            in = ParameterIn.QUERY,
                            description = "Preço máximo",
                            example = "150000")
                    @RequestParam(required = false)
                    BigDecimal precoMax,
            @Parameter(
                            name = "status",
                            in = ParameterIn.QUERY,
                            description = "Status de disponibilidade",
                            example = "DISPONIVEL")
                    @RequestParam(required = false)
                    StatusDisponibilidade status) {
        return veiculoService.listar(marca, modelo, anoMin, anoMax, precoMin, precoMax, status);
    }

    @GetMapping("/{id}")
    public VeiculoResponse buscar(@PathVariable Long id) {
        return veiculoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> criar(@Valid @RequestBody VeiculoRequest request) {
        VeiculoResponse created = veiculoService.criar(request);
        return ResponseEntity.created(URI.create("/api/veiculos/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public VeiculoResponse atualizar(@PathVariable Long id, @Valid @RequestBody VeiculoRequest request) {
        return veiculoService.atualizar(id, request);
    }

    @PatchMapping("/{id}")
    public VeiculoResponse atualizarParcial(
            @PathVariable Long id, @Valid @RequestBody VeiculoAtualizacaoRequest request) {
        return veiculoService.atualizarParcial(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        veiculoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
