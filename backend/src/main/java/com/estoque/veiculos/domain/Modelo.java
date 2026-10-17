package com.estoque.veiculos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modelos", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Modelo extends EntidadeBase {

    /** Tipo de carroceria: SUV, Sedan, Hatch, Picape, etc. */
    @Column(nullable = false, length = 60)
    private String nome;

    @Builder.Default
    @OneToMany(mappedBy = "modelo")
    private List<Veiculo> veiculos = new ArrayList<>();
}
