#!/bin/bash

PROJECT_ID=$(gcloud config list --format 'value(core.project)')

mvn  -DskipTests=true clean package

export SPRING_CLOUD_GCP_CREDENTIALS_ENCODED_KEY=$( cat $GCP_SERVICE_ACCOUNT_KEY_FILE | base64 -w0  )
export SPRING_CLOUD_GCP_PROJECT_ID=$PROJECT_ID

mvn spring-boot:run