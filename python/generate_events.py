#!/usr/bin/env python3
import random
import uuid
from datetime import datetime, timedelta, timezone

# ===== Config =====
OUT_FILE = "data.sql"

TOTAL_EVENTS_WEEK = 10_000   # eventos dentro da última semana (base do ranking)
OLD_EVENTS = 0               # coloque 20_000 ou 100_000 depois pra simular crescimento histórico

WEEK_DAYS = 7
OLD_DAYS_RANGE = 180         # eventos antigos espalhados até 180 dias atrás

SURVIVOR_COUNT = 500
REGIONS = ["rj", "sp", "mg", "es", "ba", "pe", "rs"]

EVENT_TYPES = [
    ("THREAT_DEFEATED", (5, 20)),
    ("SUPPLY_FOUND", (1, 10)),
    ("MISSION_COMPLETED", (10, 40)),
    ("TRADE_COMPLETED", (1, 8)),
    ("ALLY_RESCUED", (15, 50)),
    ("BASE_UPGRADED", (8, 30)),
]

TABLE_NAME = "survivor_activity_event"
# ===================


def weighted_recent_timestamp(start: datetime, end: datetime, k: float = 4.0) -> datetime:
    """
    Timestamp com viés para o final (mais recente).
    k maior -> mais puxado pro 'end'
    """
    r = random.random()
    t = 1.0 - (r ** k)
    delta = end - start
    return start + timedelta(seconds=int(delta.total_seconds() * t))


def uniform_timestamp(start: datetime, end: datetime) -> datetime:
    delta = end - start
    return start + timedelta(seconds=random.randint(0, int(delta.total_seconds())))


def sql_escape(s: str) -> str:
    return s.replace("'", "''")


def make_insert(survivor_id: int, occurred_at: datetime, event_type: str, score_value: int,
                region: str, event_key: str, description: str) -> str:
    occurred_at_str = occurred_at.strftime("%Y-%m-%d %H:%M:%S")
    return (
        f"INSERT INTO {TABLE_NAME} "
        f"(survivor_id, occurred_at, event_type, score_value, region, event_key, description) "
        f"VALUES ("
        f"{survivor_id}, "
        f"TIMESTAMP '{occurred_at_str}', "
        f"'{event_type}', "
        f"{score_value}, "
        f"'{sql_escape(region)}', "
        f"'{sql_escape(event_key)}', "
        f"'{sql_escape(description)}'"
        f");"
    )


def main():
    random.seed(42)
    now = datetime.now(timezone.utc)

    week_start = now - timedelta(days=WEEK_DAYS)
    old_start = now - timedelta(days=OLD_DAYS_RANGE)

    survivors = list(range(1, SURVIVOR_COUNT + 1))

    lines = []
    lines.append(f"-- Seed para {TABLE_NAME}")
    lines.append(f"-- Eventos da última semana: {TOTAL_EVENTS_WEEK}")
    if OLD_EVENTS > 0:
        lines.append(f"-- Eventos antigos adicionais: {OLD_EVENTS} (até {OLD_DAYS_RANGE} dias atrás)")
    lines.append("-- Datas em UTC")
    lines.append("")

    # 1) Eventos da última semana (base do ranking)
    for _ in range(TOTAL_EVENTS_WEEK):
        survivor_id = random.choice(survivors)
        occurred_at = weighted_recent_timestamp(week_start, now, k=4.5)  # bem recente
        event_type, (min_score, max_score) = random.choice(EVENT_TYPES)
        score_value = random.randint(min_score, max_score)
        region = random.choice(REGIONS)

        event_key = f"wk:{event_type.lower()}:{uuid.uuid4()}"
        description = f"{event_type} (week) for survivor {survivor_id} in {region}"

        lines.append(make_insert(survivor_id, occurred_at, event_type, score_value, region, event_key, description))

    # 2) Eventos antigos (opcional) pra simular “tabela gigante”
    # Isso não entra no ranking semanal, mas pesa no banco se sua query não filtrar direito / sem índices.
    for _ in range(OLD_EVENTS):
        survivor_id = random.choice(survivors)
        occurred_at = uniform_timestamp(old_start, week_start)
        event_type, (min_score, max_score) = random.choice(EVENT_TYPES)
        score_value = random.randint(min_score, max_score)
        region = random.choice(REGIONS)

        event_key = f"old:{event_type.lower()}:{uuid.uuid4()}"
        description = f"{event_type} (old) for survivor {survivor_id} in {region}"

        lines.append(make_insert(survivor_id, occurred_at, event_type, score_value, region, event_key, description))

    with open(OUT_FILE, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

    print(f"Gerado: {OUT_FILE} (week={TOTAL_EVENTS_WEEK}, old={OLD_EVENTS})")


if __name__ == "__main__":
    main()