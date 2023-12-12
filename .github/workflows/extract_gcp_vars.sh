#!/bin/bash

echo "GCP_REGION=$(jq -r '.GCP_REGION' secrets.json)" >> "$GITHUB_ENV"
echo "GCP_PROJECT_ID=$(jq -r '.GCP_PROJECT_ID' secrets.json)" >> "$GITHUB_ENV"
echo "GCP_APP=$(jq -r '.GCP_APP' secrets.json)" >> "$GITHUB_ENV"
echo "DOCKER_NAME_CONTAINER=$(jq -r '.DOCKER_NAME_CONTAINER' secrets.json)" >> "$GITHUB_ENV"
echo "SERVICE_SERVER_PORT=$(jq -r '.SERVICE_SERVER_PORT' secrets.json)" >> "$GITHUB_ENV"
echo "SQL_INSTANCE_NAME=$(jq -r '.SQL_INSTANCE_NAME' secrets.json)" >> "$GITHUB_ENV"
echo "GCP_SQL_ACCOUNT_SERVICE_NAME=$(jq -r '.GCP_SQL_ACCOUNT_SERVICE_NAME' secrets.json)" >> "$GITHUB_ENV"
