#!/bin/bash

set -e # 에러 발생 시 즉시 스크립트 종료
echo "localStack 초기화 스크립트 실행 시작.."

# 버킷 생성
echo "S3 버킷 생성: mini-drive-dev"
awslocal s3 mb s3://mini-drive-dev

#SQS 