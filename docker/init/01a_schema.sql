CREATE SCHEMA IF NOT EXISTS operacoes;

CREATE TABLE operacoes.alertas_suspeitas (
                                             id BIGSERIAL PRIMARY KEY,

                                             alerta_id VARCHAR(50) UNIQUE NOT NULL,

                                             titulo VARCHAR(255) NOT NULL,
                                             descricao TEXT,

                                             tipo VARCHAR(50) NOT NULL,
                                             gravidade VARCHAR(20) NOT NULL,
                                             status VARCHAR(30) NOT NULL,

                                             regiao VARCHAR(100),

                                             local_texto VARCHAR(255),

                                             latitude DOUBLE PRECISION,
                                             longitude DOUBLE PRECISION,

                                             fonte VARCHAR(50),

                                             confiabilidade INTEGER,

                                             data_hora TIMESTAMP NOT NULL,

                                             palavras_chave JSONB,
                                             sobreviventes_relacionados JSONB,
                                             comunidades_relacionadas JSONB,

                                             responsavel_registro VARCHAR(100),

                                             observacoes_internas TEXT,

                                             criado_em TIMESTAMP DEFAULT NOW(),
                                             atualizado_em TIMESTAMP DEFAULT NOW()
);