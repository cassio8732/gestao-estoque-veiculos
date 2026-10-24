package com.estoque.veiculos.web;

import com.estoque.veiculos.service.ModeloService;
import com.estoque.veiculos.web.dto.ModeloRequest;
import com.estoque.veiculos.web.dto.ModeloResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/modelos")
@RequiredArgsConstructor
@Tag(name = "Tipos de modelo", description = "Tipos de carroceria: SUV, Sedan, Hatch, Picape")
public class ModeloController {

    private final ModeloService modeloService;

    @GetMapping
    public List<ModeloResponse> listar() {
        return modeloService.listar();
    }

    @GetMapping("/{id}")
    public ModeloResponse buscar(@PathVariable Long id) {
        return modeloService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ModeloResponse> criar(@Valid @RequestBody ModeloRequest request) {
        ModeloResponse created = modeloService.criar(request);
        return ResponseEntity.created(URI.create("/api/modelos/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public ModeloResponse atualizar(@PathVariable Long id, @Valid @RequestBody ModeloRequest request) {
        return modeloService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        modeloService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
