package com.estoque.veiculos.repository;

import com.estoque.veiculos.domain.Modelo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    Optional<Modelo> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
