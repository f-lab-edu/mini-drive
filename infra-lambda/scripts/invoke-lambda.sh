#!/bin/bash
set -e

awslocal lambda invoke \
  --function-name "$FUNCTION_NAME" \
  --payload '{"key":"value"}' \
  --region "$REGION" \
  --endpoint-url="$END_POINT_URL" \
  test-out.json