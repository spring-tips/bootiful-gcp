#!/bin/bash

APP_NAME=gcp-vision
PROJECT_ID=$(gcloud config list --format 'value(core.project)')
GSA=$( cat $HOME/Desktop/gcp-service-app.json |  base64  )

# it's important to enquote the field!
mvn -DskipTests=true clean package
cf push --no-start -p ./target/gcp-vision-0.0.1-SNAPSHOT.jar $APP_NAME
cf set-env $APP_NAME SPRING_CLOUD_GCP_CREDENTIALS_ENCODED_KEY "$GSA"
cf set-env $APP_NAME SPRING_CLOUD_GCP_PROJECT_ID $PROJECT_ID
cf restart $APP_NAME
