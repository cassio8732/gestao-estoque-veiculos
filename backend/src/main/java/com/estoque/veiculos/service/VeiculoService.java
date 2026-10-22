package com.estoque.veiculos.service;

import com.estoque.veiculos.domain.Marca;
import com.estoque.veiculos.domain.Modelo;
import com.estoque.veiculos.domain.StatusDisponibilidade;
import com.estoque.veiculos.domain.Veiculo;
import com.estoque.veiculos.exception.BusinessException;
import com.estoque.veiculos.exception.ResourceNotFoundException;
import com.estoque.veiculos.repository.VeiculoRepository;
import com.estoque.veiculos.web.dto.VeiculoAtualizacaoRequest;
import com.estoque.veiculos.web.dto.VeiculoRequest;
import com.estoque.veiculos.web.dto.VeiculoResponse;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ModeloService modeloService;
    private final MarcaService marcaService;

    @Transactional(readOnly = true)
    public List<VeiculoResponse> listar(
            String marca,
            String modelo,
            Integer anoMin,
            Integer anoMax,
            BigDecimal precoMin,
            BigDecimal precoMax,
            StatusDisponibilidade status) {
        var spec = VeiculoSpecification.comFiltros(marca, modelo, anoMin, anoMax, precoMin, precoMax, status);
        return veiculoRepository.findAll(spec).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VeiculoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public VeiculoResponse criar(VeiculoRequest request) {
        Marca marca = marcaService.buscarPorNome(request.marca());
        Modelo modelo = modeloService.buscarPorNome(request.modelo());
        Veiculo veiculo = Veiculo.builder()
                .marca(marca)
                .modelo(modelo)
                .ano(request.ano())
                .cor(request.cor().trim())
                .preco(request.preco())
                .quilometragem(request.quilometragem())
                .status(request.status())
                .build();
        return toResponse(veiculoRepository.save(veiculo));
    }

    @Transactional
    public VeiculoResponse atualizar(Long id, VeiculoRequest request) {
        Veiculo veiculo = buscarEntidade(id);
        Marca marca = marcaService.buscarPorNome(request.marca());
        Modelo modelo = modeloService.buscarPorNome(request.modelo());
        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setAno(request.ano());
        veiculo.setCor(request.cor().trim());
        veiculo.setPreco(request.preco());
        veiculo.setQuilometragem(request.quilometragem());
        veiculo.setStatus(request.status());
        return toResponse(veiculoRepository.save(veiculo));
    }

    @Transactional
    public VeiculoResponse atualizarParcial(Long id, VeiculoAtualizacaoRequest request) {
        if (request.preco() == null && request.quilometragem() == null && request.status() == null) {
            throw new BusinessException("Informe ao menos um campo para atualização (preco, quilometragem ou status)");
        }
        Veiculo veiculo = buscarEntidade(id);
        if (request.preco() != null) {
            veiculo.setPreco(request.preco());
        }
        if (request.quilometragem() != null) {
            veiculo.setQuilometragem(request.quilometragem());
        }
        if (request.status() != null) {
            veiculo.setStatus(request.status());
        }
        return toResponse(veiculoRepository.save(veiculo));
    }

    @Transactional
    public void remover(Long id) {
        Veiculo veiculo = buscarEntidade(id);
        veiculoRepository.delete(veiculo);
    }

    @Transactional(readOnly = true)
    public Veiculo buscarEntidade(Long id) {
        return veiculoRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: id=" + id));
    }

    private VeiculoResponse toResponse(Veiculo veiculo) {
        Marca marca = veiculo.getMarca();
        Modelo modelo = veiculo.getModelo();
        return new VeiculoResponse(
                veiculo.getId(),
                modelo.getId(),
                modelo.getNome(),
                marca.getId(),
                marca.getNome(),
                veiculo.getAno(),
                veiculo.getCor(),
                veiculo.getPreco(),
                veiculo.getQuilometragem(),
                veiculo.getStatus());
    }
}
