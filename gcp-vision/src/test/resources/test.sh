#!/usr/bin/env bash

# if you deploy this to Cloud Foundry then specify the CF URI (look at the output of cf apps for the URL)
URL=${GCP_VISION_URL_ROOT:-http://localhost:8080}
curl  -F "image=@./soup.jpg" ${URL}/analyze
