#!/bin/bash

echo "Using the service-account.json file ${GCP_SERVICE_ACCOUNT_KEY_FILE}..."

APP_NAME=gcp-vision
PROJECT_ID=$(gcloud config list --format 'value(core.project)')
GSA=$( cat $GCP_SERVICE_ACCOUNT_KEY_FILE | base64 -w0 )

mvn -DskipTests=true clean package

cf d -f $APP_NAME
cf push --no-start --random-route -p ./target/gcp-vision-0.0.1-SNAPSHOT.jar $APP_NAME
cf set-env $APP_NAME SPRING_CLOUD_GCP_CREDENTIALS_ENCODED_KEY "$GSA"
cf set-env $APP_NAME SPRING_CLOUD_GCP_PROJECT_ID $PROJECT_ID
cf restart $APP_NAME
