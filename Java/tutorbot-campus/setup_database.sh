#!/usr/bin/env bash

# Copies SQL migration scripts into the Oracle container and runs them via sqlplus.
# Run this from the project root after the oracledb container is healthy.

set -euo pipefail

CONTAINER="oracledb"
DB_USER="Adolfo"
DB_PASS="password"
DB_SERVICE="XEPDB1"
DB_PORT="1521"   # internal container port

SCHEMA_SQL="infrastructure/oracle/V1__init_schema.sql"
SEED_SQL="infrastructure/oracle/V2__seed_data.sql"

# ─── Wait for the container to be ready ───────────────────────────────────────
echo "Waiting for $CONTAINER to be healthy..."
until docker exec "$CONTAINER" healthcheck.sh &>/dev/null; do
  printf '.'
  sleep 5
done
echo ""
echo "Container is ready."

# ─── Run V1 — Schema (tables, sequences, indexes) ─────────────────────────────
echo "Copying $SCHEMA_SQL into container..."
docker cp "$SCHEMA_SQL" "$CONTAINER:/tmp/V1__init_schema.sql"

echo "Running V1__init_schema.sql..."
MSYS_NO_PATHCONV=1 docker exec \
  -e NLS_LANG=AMERICAN_AMERICA.AL32UTF8 \
  "$CONTAINER" \
  sqlplus -s "$DB_USER/$DB_PASS@//localhost:$DB_PORT/$DB_SERVICE" @/tmp/V1__init_schema.sql

echo "Schema created."

# ─── Run V2 — Seed data ───────────────────────────────────────────────────────
echo "Copying $SEED_SQL into container..."
docker cp "$SEED_SQL" "$CONTAINER:/tmp/V2__seed_data.sql"

echo "Running V2__seed_data.sql..."
MSYS_NO_PATHCONV=1 docker exec \
  -e NLS_LANG=AMERICAN_AMERICA.AL32UTF8 \
  "$CONTAINER" \
  sqlplus -s "$DB_USER/$DB_PASS@//localhost:$DB_PORT/$DB_SERVICE" @/tmp/V2__seed_data.sql

echo "Seed data loaded."
echo ""
echo "Done. Your Oracle schema is ready."
