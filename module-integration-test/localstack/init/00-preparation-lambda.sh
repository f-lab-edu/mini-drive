#!/bin/bash
set -e

echo "Localstack 컨테이너 Lambda 배포 Gradle Task 실행"
awslocal lambda update-function-code /
  --function-name updateCallbackFunction /
  --zip-file
