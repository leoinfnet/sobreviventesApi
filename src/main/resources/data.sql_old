-- ================================
-- COMUNIDADES
-- ================================

INSERT INTO comunidades (nome, zona_segura) VALUES ('Abrigo Central', TRUE);
INSERT INTO comunidades (nome, zona_segura) VALUES ('Mercado Cinzento', FALSE);
INSERT INTO comunidades (nome, zona_segura) VALUES ('Torre de Radio', FALSE);
INSERT INTO comunidades (nome, zona_segura) VALUES ('Hospital Abandonado', FALSE);
INSERT INTO comunidades (nome, zona_segura) VALUES ('Bunker Subterraneo', TRUE);


-- ================================
-- SOBREVIVENTES
-- ================================

INSERT INTO sobreviventes (nome, localizacao, infectado)
VALUES ('Mara', 'Setor 7 - Norte', FALSE);

INSERT INTO sobreviventes (nome, localizacao, infectado)
VALUES ('Dante', 'Estacao Antiga - Plataforma 2', FALSE);

INSERT INTO sobreviventes (nome, localizacao, infectado)
VALUES ('Iris', 'Tunel Leste - Entrada 3', TRUE);

INSERT INTO sobreviventes (nome, localizacao, infectado)
VALUES ('Noah', 'Zona Industrial - Galpao 9', FALSE);

INSERT INTO sobreviventes (nome, localizacao, infectado)
VALUES ('Ravi', 'Ponte Quebrada - Margem Sul', TRUE);


-- ================================
-- RECURSOS (OneToMany)
-- ================================

-- Mara (id 1)
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Agua', 10, 1);
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Comida', 5, 1);
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Remedio', 2, 1);

-- Dante (id 2)
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Municao', 20, 2);
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Lanterna', 1, 2);

-- Iris (id 3)
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Mapa', 1, 3);

-- Noah (id 4)
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Ferro', 7, 4);
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Corda', 1, 4);

-- Ravi (id 5)
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Agua', 3, 5);
INSERT INTO recursos (nome, quantidade, sobrevivente_id) VALUES ('Comida', 2, 5);


-- ================================
-- MANY TO MANY (JOIN TABLE)
-- ================================

-- Mara
INSERT INTO sobrevivente_comunidade (comunidade_id, sobrevivente_id) VALUES (1, 1);
INSERT INTO sobrevivente_comunidade (comunidade_id, sobrevivente_id) VALUES (2, 1);

-- Dante
INSERT INTO sobrevivente_comunidade (comunidade_id, sobrevivente_id) VALUES (2, 2);

-- Iris
INSERT INTO sobrevivente_comunidade (comunidade_id, sobrevivente_id) VALUES (3, 3);

-- Noah
INSERT INTO sobrevivente_comunidade (comunidade_id, sobrevivente_id) VALUES (1, 4);
INSERT INTO sobrevivente_comunidade (comunidade_id, sobrevivente_id) VALUES (5, 4);

-- Ravi
INSERT INTO sobrevivente_comunidade (comunidade_id, sobrevivente_id) VALUES (4, 5);