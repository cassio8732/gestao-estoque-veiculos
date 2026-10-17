package com.estoque.veiculos.repository;

import com.estoque.veiculos.domain.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {

    boolean existsByModeloId(Long modeloId);

    boolean existsByMarcaId(Long marcaId);
}
