#!/bin/bash

# Shell script to generate necessary DDB tables for specified region and
# AWS account.

while [[ $# -gt 0 ]]; do
  key="$1"
  case $key in
    -r|--region)
      REGION="$2"
      shift # past argument
      shift # past value
      ;;
    -a|--accountId)
      ACCOUNT="$2"
      shift # past argument
      shift # past value
      ;;
    -s|--secretKey)
      SECRET="$2"
      shift # past argument
      shift # past value
      ;;
  esac
done

export AWS_ACCESS_KEY_ID=${ACCOUNT}
export AWS_SECRET_ACCESS_KEY=${SECRET}
export AWS_DEFAULT_REGION=${REGION}

aws dynamodb create-table \
    --table-name Employee \
    --key-schema \
        AttributeName=employeeId,KeyType=HASH \
    --attribute-definitions \
        AttributeName=employeeId,AttributeType=S \
    --provisioned-throughput \
        ReadCapacityUnits=10,WriteCapacityUnits=5

unset AWS_ACCESS_KEY_ID
unset AWS_SECRET_ACCESS_KEY
unset AWS_DEFAULT_REGION