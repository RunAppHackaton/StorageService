#!/bin/bash

docker buildx create --use
docker buildx build \
  --file ./dockerfile \
  --tag $GCP_REGION-docker.pkg.dev/$GCP_PROJECT_ID/$GCP_APP/$DOCKER_NAME_CONTAINER:$IMAGE_TAG \
  --push .