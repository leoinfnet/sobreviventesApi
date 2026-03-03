#!/usr/bin/env python3
import random
import uuid
from datetime import datetime, timedelta, timezone

# ===== Config =====
OUT_FILE = "../docker/init/03_eventos.sql"

TOTAL_EVENTS = 100_000        # total geral (semana + antigos)
WEEK_RATIO = 0.10            # 10% na última semana, 90% antigos

WEEK_DAYS = 7
OLD_DAYS_RANGE = 180         # antigos até 180 dias atrás (você pode aumentar depois)

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
SEQUENCE_NAME = "survivor_activity_event_seq"
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
        f"(id, survivor_id, occurred_at, event_type, score_value, region, event_key, description) "
        f"VALUES ("
        f"nextval('{SEQUENCE_NAME}'), "
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

    # Eventos antigos: de (week_start - OLD_DAYS_RANGE) até (week_start - 1 segundo)
    old_start = week_start - timedelta(days=OLD_DAYS_RANGE)
    old_end = week_start - timedelta(seconds=1)

    week_events = int(TOTAL_EVENTS * WEEK_RATIO)
    old_events = TOTAL_EVENTS - week_events

    survivors = list(range(1, SURVIVOR_COUNT + 1))

    lines = []
    lines.append(f"-- Seed para {TABLE_NAME}")
    lines.append(f"-- Usando sequence: {SEQUENCE_NAME}")
    lines.append(f"-- TOTAL_EVENTS={TOTAL_EVENTS}")
    lines.append(f"-- Semana (últimos {WEEK_DAYS} dias): {week_events} eventos ({int(WEEK_RATIO*100)}%)")
    lines.append(f"-- Antigos (antes da semana, até {OLD_DAYS_RANGE} dias): {old_events} eventos ({100-int(WEEK_RATIO*100)}%)")
    lines.append("-- Datas em UTC")
    lines.append("")

    # 1) Eventos da última semana (10%)
    for _ in range(week_events):
        survivor_id = random.choice(survivors)
        occurred_at = weighted_recent_timestamp(week_start, now, k=4.5)
        event_type, (min_score, max_score) = random.choice(EVENT_TYPES)
        score_value = random.randint(min_score, max_score)
        region = random.choice(REGIONS)

        event_key = f"wk:{event_type.lower()}:{uuid.uuid4()}"
        description = f"{event_type} (week) for survivor {survivor_id} in {region}"

        lines.append(make_insert(survivor_id, occurred_at, event_type, score_value, region, event_key, description))

    # 2) Eventos antigos (90%) — poluição proposital
    for _ in range(old_events):
        survivor_id = random.choice(survivors)
        occurred_at = uniform_timestamp(old_start, old_end)  # uniformemente espalhado no histórico
        event_type, (min_score, max_score) = random.choice(EVENT_TYPES)
        score_value = random.randint(min_score, max_score)
        region = random.choice(REGIONS)

        event_key = f"old:{event_type.lower()}:{uuid.uuid4()}"
        description = f"{event_type} (old) for survivor {survivor_id} in {region}"

        lines.append(make_insert(survivor_id, occurred_at, event_type, score_value, region, event_key, description))

    with open(OUT_FILE, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

    print(f"Gerado: {OUT_FILE} (week={week_events}, old={old_events}, total={TOTAL_EVENTS})")


if __name__ == "__main__":
    main()