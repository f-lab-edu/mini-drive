#!/bin/bash
set -e
echo "🔄 Lambda 패키징 중..."

cd /tmp
rm -f lambda.zip
zip -j /tmp/lambda.zip /etc/localstack/lambda/* > /dev/null

# S3 버킷 생성
awslocal s3 mb s3://mini-drive-dev-upload

# SQS 큐 생성
awslocal sqs create-queue --queue-name upload-callback-queue

#lambda 함수 등록
awslocal lambda create-function \
  --function-name uploadCallbackFunction \
  --runtime nodejs18.x \
  --handler index.handler \
  --role arn:aws:iam::000000000000:role/lambda-role \
  --zip-file fileb:///tmp/lambda.zip

echo "⏳ Lambda 초기화 대기 중..."
awslocal lambda wait function-active --function-name uploadCallbackFunction

# Lambda -> SQS Trigger 연결
QUEUE_ARN=$(awslocal sqs get-queue-attributes \
  --queue-url http://localhost:4566/000000000000/upload-callback-queue \
  --attribute-name QueueArn \
  --query "Attributes.QueueArn" --output text)

awslocal lambda create-event-source-mapping \
  --function-name uploadCallbackFunction \
  --batch-size 1 \
  --event-source-arn "$QUEUE_ARN"

# S3 → SQS 이벤트 연결
awslocal --endpoint-url=http://localhost:4566 s3api put-bucket-notification-configuration \
  --bucket mini-drive-dev-upload \
  --notification-configuration '{
    "QueueConfigurations": [
      {
        "QueueArn": "'"$QUEUE_ARN"'",
        "Events": ["s3:ObjectCreated:*"]
      }
    ]
  }'

  echo "✅ 초기화 완료!"