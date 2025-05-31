#!/bin/bash
set -e

echo "trigger" > trigger.txt && \
  awslocal s3 cp trigger.txt s3://"$BUCKET_NAME"/"$UPLOAD_PREFIX"/"$FILE_NAME" \
  --endpoint-url="$ENDPOINT_URL"