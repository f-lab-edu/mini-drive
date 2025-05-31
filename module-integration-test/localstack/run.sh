#!/bin/bash

cd "$(dirname "$0")" || exit 1

ROOT_DIR="$(git rev-parse --show-toplevel)"
INIT_DIR="$(cd "$(dirname "$0")" && pwd)/init"
LAMBDA_DIR="$ROOT_DIR/build/lambda-zip"

echo "INIT_DIR is: $INIT_DIR"
echo "ROOT_DIR is: $ROOT_DIR"


export TMPDIR=/private$TMPDIR
export INIT_DIR
export ROOT_DIR
export LAMBDA_DIR

# 실행 권한 부여
chmod +x "$INIT_DIR"/*.sh

docker compose -f docker-compose.local.yml up -d