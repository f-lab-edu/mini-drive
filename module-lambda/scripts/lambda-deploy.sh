#!/bin/bash
set -e

echo "✅ 전달된 ZIP_PATH: [$ZIP_PATH]"
ls -lh "$ZIP_PATH" || echo "❌ 존재하지 않음"

if awslocal lambda get-function \
  --function-name "$FUNCTION_NAME" \
  --endpoint-url="$ENDPOINT_URL" &> /dev/null; then
  echo "Lambda 함수가 존재하므로 업데이트합니다..."
  awslocal lambda invoke \
    --function-name "$FUNCTION_NAME" \
    --payload '{}' \
    --region "$REGION" \
    --endpoint-url="$ENDPOINT_URL" \
    /dev/null || true

  awslocal lambda update-function-code \
    --function-name "$FUNCTION_NAME" \
    --zip-file "fileb://$ZIP_PATH" \
    --region "$REGION" \
    --endpoint-url="$ENDPOINT_URL"
else
  echo "Lambda 함수가 없으므로 새로 생성합니다..."
  awslocal lambda create-function \
    --function-name "$FUNCTION_NAME" \
    --runtime nodejs18.x \
    --handler index.handler \
    --role arn:aws:iam::000000000000:role/dummy-role \
    --zip-file "fileb://$ZIP_PATH" \
    --region "$REGION" \
    --endpoint-url="$ENDPOINT_URL"
fi