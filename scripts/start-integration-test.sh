#!/bin/zsh
set -e

echo "Lambda zip 빌드 중..."
./gradlew prepareLambdaForLocalStack || { echo "빌드 실패"; exit 1; }

echo "🐳 LocalStack 컨테이너 시작 중..."
docker compose -f docker-compose.local.yml up -d