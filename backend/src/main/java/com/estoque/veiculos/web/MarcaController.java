package com.estoque.veiculos.web;

import com.estoque.veiculos.service.MarcaService;
import com.estoque.veiculos.web.dto.MarcaRequest;
import com.estoque.veiculos.web.dto.MarcaResponse;
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
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    public List<MarcaResponse> listar() {
        return marcaService.listar();
    }

    @GetMapping("/{id}")
    public MarcaResponse buscar(@PathVariable Long id) {
        return marcaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<MarcaResponse> criar(@Valid @RequestBody MarcaRequest request) {
        MarcaResponse created = marcaService.criar(request);
        return ResponseEntity.created(URI.create("/api/marcas/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public MarcaResponse atualizar(@PathVariable Long id, @Valid @RequestBody MarcaRequest request) {
        return marcaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        marcaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
