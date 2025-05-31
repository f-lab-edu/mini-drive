#!/bin/bash
set -e

echo "S3 버킷 생성: $BUCKET_NAME"

if awslocal s3api head-bucket --bucket "$BUCKET_NAME" --endpoint-url "$ENDPOINT_URL" 2>/dev/null; then
  echo "✅ 이미 존재하는 버킷입니다: $BUCKET_NAME"
else
awslocal s3api create-bucket \
  --bucket "$BUCKET_NAME" \
  --endpoint-url "$ENDPOINT_URL" \
  --region "$REGION" \
  --create-bucket-configuration LocationConstraint="$REGION"
fi