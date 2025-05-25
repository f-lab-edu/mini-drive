#!/bin/bash
set -e

awslocal lambda update-function-code \
  --function-name uploadCallbackFunction \
  --zip-file fileb://build/lambda/index.zip \
  --endpoint-url http://localhost:4566 \
  --region ap-northeast-2