#!/usr/bin/env bash

mvn -DskipTests=true clean package

# <1>
cf d -f $APP_NAME

# <2>
cf push --no-start --random-route -p $JAR $APP_NAME
cf set-env $APP_NAME SPRING_CLOUD_GCP_CREDENTIALS_ENCODED_KEY "$( cat $GCP_SERVICE_ACCOUNT_KEY_FILE | base64 -w0 )"
cf set-env $APP_NAME SPRING_CLOUD_GCP_PROJECT_ID $(gcloud config list --format 'value(core.project)')

# <3>
cf restart $APP_NAME