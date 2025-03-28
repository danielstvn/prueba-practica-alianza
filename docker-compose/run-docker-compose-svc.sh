#!/bin/bash
RED=`tput setaf 1`
GREEN=`tput setaf 2`
BLUE=`tput setaf 4`
RESET=`tput sgr0`

# Ruta absoluta al directorio PostgreSQL
POSTGRESQL_DB_DIR="/byte/docker/volumes/postgresdb"

# Crear directorio para PostgreSQL si no existe
if ! [ -d "$POSTGRESQL_DB_DIR" ]; then
    echo "Creating postgres-db dir volume..."
    sudo mkdir -p $POSTGRESQL_DB_DIR
    sleep 1
    echo "${GREEN}Directory: $POSTGRESQL_DB_DIR created successfully...${RESET}"
fi

# Verificar si la red existe y crear sólo si no existe
if ! docker network inspect byte-net >/dev/null 2>&1; then
    echo "Creating byte-net network..."
    docker network create --driver=bridge byte-net
    sleep 1
else
    echo "${BLUE}Network byte-net already exists, skipping creation...${RESET}"
fi

echo "Deploying base services..."
docker compose -f docker-compose.yml -p byte-services up -d --build
