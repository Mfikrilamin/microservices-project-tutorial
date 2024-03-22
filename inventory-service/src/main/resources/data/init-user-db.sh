#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER fikri;
	CREATE DATABASE inventoryservice;
	GRANT ALL PRIVILEGES ON DATABASE inventoryservice TO fikri;
EOSQL