#!/usr/bin/env bash

D=`dirname $0`
# if you deploy this to Cloud Foundry then specify the CF URI (look at the output of cf apps for the URL)
URL=${GCP_VISION_URL_ROOT:-http://localhost:8080}
curl  -F "image=@${D}/soup.jpg" ${URL}/analyze
