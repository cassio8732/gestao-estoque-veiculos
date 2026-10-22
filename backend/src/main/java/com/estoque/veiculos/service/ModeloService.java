package com.estoque.veiculos.service;

import com.estoque.veiculos.domain.Modelo;
import com.estoque.veiculos.exception.BusinessException;
import com.estoque.veiculos.exception.ResourceNotFoundException;
import com.estoque.veiculos.repository.ModeloRepository;
import com.estoque.veiculos.repository.VeiculoRepository;
import com.estoque.veiculos.web.dto.ModeloRequest;
import com.estoque.veiculos.web.dto.ModeloResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final VeiculoRepository veiculoRepository;

    @Transactional(readOnly = true)
    public List<ModeloResponse> listar() {
        return modeloRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ModeloResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public ModeloResponse criar(ModeloRequest request) {
        String nome = normalizarNome(request.nome());
        if (modeloRepository.existsByNomeIgnoreCase(nome)) {
            throw new BusinessException("Já existe um tipo de modelo com este nome");
        }
        Modelo modelo = Modelo.builder().nome(nome).build();
        return toResponse(modeloRepository.save(modelo));
    }

    @Transactional
    public ModeloResponse atualizar(Long id, ModeloRequest request) {
        Modelo modelo = buscarEntidade(id);
        String nome = normalizarNome(request.nome());
        modeloRepository
                .findByNomeIgnoreCase(nome)
                .filter(m -> !m.getId().equals(id))
                .ifPresent(m -> {
                    throw new BusinessException("Já existe um tipo de modelo com este nome");
                });
        modelo.setNome(nome);
        return toResponse(modeloRepository.save(modelo));
    }

    @Transactional
    public void remover(Long id) {
        Modelo modelo = buscarEntidade(id);
        if (veiculoRepository.existsByModeloId(id)) {
            throw new BusinessException("Não é possível remover tipo de modelo com veículos associados");
        }
        modeloRepository.delete(modelo);
    }

    @Transactional(readOnly = true)
    public Modelo buscarEntidade(Long id) {
        return modeloRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de modelo não encontrado: id=" + id));
    }

    @Transactional(readOnly = true)
    public Modelo buscarPorNome(String nome) {
        return modeloRepository
                .findByNomeIgnoreCase(normalizarNome(nome))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de modelo \"" + nome.trim() + "\" não encontrado. "
                                + "Use SUV, Sedan, Hatch ou Picape, ou cadastre em POST /api/modelos"));
    }

    private static String normalizarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return nome;
        }
        String t = nome.trim();
        return switch (t.toLowerCase()) {
            case "suv" -> "SUV";
            case "sedan", "sedã" -> "Sedan";
            case "hatch", "ret", "hatchback" -> "Hatch";
            case "picape", "pickup", "caminhonete" -> "Picape";
            case "perua", "wagon", "sw" -> "Perua";
            default -> t.substring(0, 1).toUpperCase() + t.substring(1).toLowerCase();
        };
    }

    private ModeloResponse toResponse(Modelo modelo) {
        return new ModeloResponse(modelo.getId(), modelo.getNome());
    }
}
