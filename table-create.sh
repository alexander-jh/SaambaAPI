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
    --table-name Music \
    --attribute-definitions \
        AttributeName=genre,AttributeType=S \
        AttributeName=uri,AttributeType=S \
    --key-schema \
        AttributeName=genre,KeyType=HASH \
        AttributeName=uri,KeyType=RANGE \
    --provisioned-throughput \
        ReadCapacityUnits=10,WriteCapacityUnits=5

aws dynamodb create-table \
    --table-name Users \
    --attribute-definitions \
        AttributeName=accountName,AttributeType=S \
    --key-schema \
        AttributeName=accountName,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=10,WriteCapacityUnits=5

unset AWS_ACCESS_KEY_ID
unset AWS_SECRET_ACCESS_KEY
unset AWS_DEFAULT_REGION