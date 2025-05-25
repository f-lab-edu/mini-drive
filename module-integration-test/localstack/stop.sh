#!/bin/bash

cd "$(dirname "$0")" || exit 1

ROOT_DIR="$(git rev-parse --show-toplevel)"
INIT_DIR="$(cd "$(dirname "$0")" && pwd)/init"

export TMPDIR=/private$TMPDIR
export INIT_DIR
export ROOT_DIR

docker compose -f docker-compose.local.yml down -v