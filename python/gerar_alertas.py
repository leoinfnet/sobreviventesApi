#!/usr/bin/env python3
import random
import sys
import json
from datetime import datetime, timedelta

# -------------------------------
# CONFIGURAÇÕES
# -------------------------------

REGIOES = [
    "ZONA_NORTE",
    "ZONA_SUL",
    "CENTRO",
    "SETOR_7",
    "FRONTEIRA_LESTE",
    "PORTO_ABANDONADO"
]

TIPOS = [
    "SURTO",
    "PILHAGEM",
    "MOVIMENTACAO_ESTRANHA",
    "FOCO_INFECTADO",
    "PEDIDO_SOCORRO"
]

GRAVIDADES = ["BAIXA", "MEDIA", "ALTA", "CRITICA"]

STATUS = [
    "ABERTO",
    "EM_ANALISE",
    "CONFIRMADO",
    "DESCARTADO",
    "ENCERRADO"
]

FONTES = [
    "RADIO",
    "PATRULHA",
    "DRONE",
    "TESTEMUNHA",
    "SENSOR"
]

LOCAIS = [
    "Hospital abandonado",
    "Mercado cinzento",
    "Entrada leste do bunker",
    "Torre de rádio",
    "Antiga estação ferroviária",
    "Portão norte da cidade",
    "Complexo industrial",
    "Armazém 12"
]

TITULOS = [
    "Movimentação suspeita detectada",
    "Possível foco de infectados",
    "Pedido de socorro captado",
    "Grupo armado avistado",
    "Atividade incomum durante a madrugada",
    "Relato de barulhos metálicos",
    "Presença hostil relatada",
    "Movimentação perto do hospital"
]

DESCRICOES = [
    "Patrulha relatou movimentação estranha na área.",
    "Sinais de presença de infectados foram detectados.",
    "Testemunha informou gritos e barulhos metálicos.",
    "Possível grupo hostil operando na região.",
    "Drone captou movimentação incomum.",
    "Relato de atividade suspeita durante a madrugada."
]

RESPONSAVEIS = [
    "OPERADOR_01",
    "OPERADOR_02",
    "PATRULHA_ALPHA",
    "PATRULHA_BETA",
    "CENTRAL_RADIO"
]

BASE_LAT = -22.90
BASE_LON = -43.20


# -------------------------------
# FUNÇÕES AUXILIARES
# -------------------------------

def random_date():
    start = datetime(2019, 1, 1)
    end = datetime(2026, 12, 31)

    delta = end - start
    random_days = random.randint(0, delta.days)

    random_seconds = random.randint(0, 86400)

    return start + timedelta(days=random_days, seconds=random_seconds)


def random_geo():
    lat = BASE_LAT + random.uniform(-0.3, 0.3)
    lon = BASE_LON + random.uniform(-0.3, 0.3)
    return lat, lon


def random_keywords():
    palavras = ["infectados", "barulho", "grupo", "drone", "hospital", "socorro", "movimento"]
    return random.sample(palavras, random.randint(2, 4))


def random_ids(prefixo):
    return f"{prefixo}-{random.randint(1,999):03d}"


# -------------------------------
# GERA DOCUMENTO
# -------------------------------

def gerar_documento(i):

    alerta_id = f"ALT-{i:05d}"

    titulo = random.choice(TITULOS)
    descricao = random.choice(DESCRICOES)

    tipo = random.choice(TIPOS)
    gravidade = random.choice(GRAVIDADES)
    status = random.choice(STATUS)

    regiao = random.choice(REGIOES)

    local = random.choice(LOCAIS)

    lat, lon = random_geo()

    fonte = random.choice(FONTES)

    confiabilidade = random.randint(10, 100)

    data = random_date()

    palavras = random_keywords()

    sobreviventes = [random_ids("SURV") for _ in range(random.randint(0,3))]
    comunidades = [random_ids("COM") for _ in range(random.randint(0,2))]

    responsavel = random.choice(RESPONSAVEIS)

    observacoes = "Registro gerado automaticamente para testes."

    doc = {
        "alertaId": alerta_id,
        "titulo": titulo,
        "descricao": descricao,
        "tipo": tipo,
        "gravidade": gravidade,
        "status": status,
        "regiao": regiao,
        "localTexto": local,
        "localizacao": {
            "lat": lat,
            "lon": lon
        },
        "fonte": fonte,
        "confiabilidade": confiabilidade,
        "dataHora": data.isoformat(),
        "palavrasChave": palavras,
        "sobreviventesRelacionados": sobreviventes,
        "comunidadesRelacionadas": comunidades,
        "responsavelRegistro": responsavel,
        "observacoesInternas": observacoes
    }

    return doc, lat, lon, data


# -------------------------------
# MAIN
# -------------------------------

def main():

    if len(sys.argv) < 2:
        print("Uso: python gerar_alertas.py <quantidade>")
        return

    quantidade = int(sys.argv[1])

    sql_file = open("../docker/init/04_alertas.sql", "w")
    bulk_file = open("dados_elasticsearch_bulk.json", "w")

    sql_file.write("BEGIN;\n")

    for i in range(quantidade):

        doc, lat, lon, data = gerar_documento(i)

        # -------------------------------
        # POSTGRES
        # -------------------------------

        sql = f"""
INSERT INTO operacoes.alertas_suspeitas
(alerta_id, titulo, descricao, tipo, gravidade, status, regiao,
 local_texto, latitude, longitude, fonte, confiabilidade, data_hora,
 palavras_chave, sobreviventes_relacionados, comunidades_relacionadas,
 responsavel_registro, observacoes_internas)
VALUES
('{doc["alertaId"]}',
'{doc["titulo"]}',
'{doc["descricao"]}',
'{doc["tipo"]}',
'{doc["gravidade"]}',
'{doc["status"]}',
'{doc["regiao"]}',
'{doc["localTexto"]}',
{lat},
{lon},
'{doc["fonte"]}',
{doc["confiabilidade"]},
'{data.strftime("%Y-%m-%d %H:%M:%S")}',
'{json.dumps(doc["palavrasChave"])}',
'{json.dumps(doc["sobreviventesRelacionados"])}',
'{json.dumps(doc["comunidadesRelacionadas"])}',
'{doc["responsavelRegistro"]}',
'{doc["observacoesInternas"]}');
"""

        sql_file.write(sql)

        # -------------------------------
        # ELASTICSEARCH BULK
        # -------------------------------

        action = {"index": {"_index": "alertas_suspeitas"}}

        bulk_file.write(json.dumps(action) + "\n")
        bulk_file.write(json.dumps(doc) + "\n")

    sql_file.write("COMMIT;\n")

    sql_file.close()
    bulk_file.close()

    print("Arquivos gerados:")
    print("04_alertas.sql")
    print("dados_elasticsearch_bulk.json")


if __name__ == "__main__":
    main()