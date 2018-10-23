gcloud auth application-default login

export PROJECT_ID=$(gcloud config list --format 'value(core.project)')


# Config 
gcloud beta runtime-config configs create reservations_cloud 
gcloud beta runtime-config configs variables set greeting  "Hello GCP"  --config-name reservations_cloud
gcloud beta runtime-config configs variables list --config-name reservations_cloud