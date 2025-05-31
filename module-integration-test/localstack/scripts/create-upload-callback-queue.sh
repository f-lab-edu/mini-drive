#!/bin/bash
set -e

# SQS Queue 생성
QUEUE_URL=$(awslocal sqs create-queue \
  --queue-name "$QUEUE_NAME" \
  --query "QueueUrl" \
  --output text)

# QueueArn 획득
QUEUE_ARN=$(awslocal sqs get-queue-attributes \
  --queue-url "$QUEUE_URL" \
  --attribute-name QueueArn \
  --query "Attributes.QueueArn" \
  --output text)

# tmp-s3-notification.json 생성
cat > tmp-s3-notification.json <<EOF
{
  "QueueConfigurations": [
    {
      "QueueArn": "$QUEUE_ARN",
      "Events": ["s3:ObjectCreated:*"]
    }
  ]
}
EOF

# S3 알림 업로드 알림 이벤트 설정
awslocal s3api put-bucket-notification-configuration \
  --bucket "$BUCKET_NAME" \
  --notification-configuration file://tmp-s3-notification.json