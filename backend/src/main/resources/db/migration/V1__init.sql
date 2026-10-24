-- Referência do modelo físico; em execução local o schema é mantido pelo JPA (ddl-auto=update).

CREATE TABLE IF NOT EXISTS marcas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    criado_em TIMESTAMP(6) NOT NULL,
    atualizado_em TIMESTAMP(6) NOT NULL,
    UNIQUE KEY uk_marcas_nome (nome)
);

CREATE TABLE IF NOT EXISTS modelos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    criado_em TIMESTAMP(6) NOT NULL,
    atualizado_em TIMESTAMP(6) NOT NULL,
    UNIQUE KEY uk_modelos_nome (nome)
);

CREATE TABLE IF NOT EXISTS veiculos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    marca_id BIGINT NOT NULL,
    modelo_id BIGINT NOT NULL,
    ano INT NOT NULL,
    cor VARCHAR(60) NOT NULL,
    preco DECIMAL(12, 2) NOT NULL,
    quilometragem INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP(6) NOT NULL,
    atualizado_em TIMESTAMP(6) NOT NULL,
    CONSTRAINT fk_veiculos_marca FOREIGN KEY (marca_id) REFERENCES marcas (id),
    CONSTRAINT fk_veiculos_modelo FOREIGN KEY (modelo_id) REFERENCES modelos (id),
    KEY idx_veiculos_status (status),
    KEY idx_veiculos_ano (ano),
    KEY idx_veiculos_preco (preco)
);
