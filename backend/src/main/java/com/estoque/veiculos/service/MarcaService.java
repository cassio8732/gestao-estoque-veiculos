package com.estoque.veiculos.service;

import com.estoque.veiculos.domain.Marca;
import com.estoque.veiculos.exception.BusinessException;
import com.estoque.veiculos.exception.ResourceNotFoundException;
import com.estoque.veiculos.repository.MarcaRepository;
import com.estoque.veiculos.repository.VeiculoRepository;
import com.estoque.veiculos.web.dto.MarcaRequest;
import com.estoque.veiculos.web.dto.MarcaResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final VeiculoRepository veiculoRepository;

    @Transactional(readOnly = true)
    public List<MarcaResponse> listar() {
        return marcaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MarcaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public MarcaResponse criar(MarcaRequest request) {
        if (marcaRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new BusinessException("Já existe uma marca com este nome");
        }
        Marca marca = Marca.builder().nome(request.nome().trim()).build();
        return toResponse(marcaRepository.save(marca));
    }

    @Transactional
    public MarcaResponse atualizar(Long id, MarcaRequest request) {
        Marca marca = buscarEntidade(id);
        marcaRepository
                .findByNomeIgnoreCase(request.nome())
                .filter(m -> !m.getId().equals(id))
                .ifPresent(m -> {
                    throw new BusinessException("Já existe uma marca com este nome");
                });
        marca.setNome(request.nome().trim());
        return toResponse(marcaRepository.save(marca));
    }

    @Transactional
    public void remover(Long id) {
        buscarEntidade(id);
        if (veiculoRepository.existsByMarcaId(id)) {
            throw new BusinessException("Não é possível remover marca com veículos associados");
        }
        marcaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Marca buscarEntidade(Long id) {
        return marcaRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada: id=" + id));
    }

    @Transactional(readOnly = true)
    public Marca buscarPorNome(String nome) {
        return buscarEntidadePorNome(nome)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Marca não encontrada: \"" + nome.trim()
                                + "\". Cadastre em POST /api/marcas ou consulte GET /api/marcas"));
    }

    @Transactional(readOnly = true)
    public Optional<Marca> buscarEntidadePorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return Optional.empty();
        }
        return marcaRepository.findByNomeIgnoreCase(nome.trim());
    }

    private MarcaResponse toResponse(Marca marca) {
        return new MarcaResponse(marca.getId(), marca.getNome());
    }
}
