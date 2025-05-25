#!/bin/bash
set -e

echo "S3 버킷 생성: $BUCKET_NAME"

awslocal s3api create-bucket \
  --bucket "$BUCKET_NAME" \
  --endpoint-url "$ENDPOINT_URL" \
  --region "$REGION" \
  --create-bucket-configuration LocationConstraint="$REGION"