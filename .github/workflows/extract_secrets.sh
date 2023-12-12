#!/bin/bash

echo "DOCKER_NAME_CONTAINER=$(jq -r '.DOCKER_NAME_CONTAINER' secrets.json)" >>"$GITHUB_ENV"
echo "DOCKER_CONTAINER_TAG=$(jq -r '.DOCKER_CONTAINER_TAG' secrets.json)" >>"$GITHUB_ENV"
echo "DOCKER_USERNAME=$(jq -r '.DOCKER_USERNAME' secrets.json)" >>"$GITHUB_ENV"
echo "DOCKER_PASSWORD=$(jq -r '.DOCKER_PASSWORD' secrets.json)" >>"$GITHUB_ENV"