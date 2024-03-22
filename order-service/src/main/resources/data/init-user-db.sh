#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER fikri;
	CREATE DATABASE orderservice;
	GRANT ALL PRIVILEGES ON DATABASE orderservice TO fikri;
EOSQL