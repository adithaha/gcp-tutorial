# Get GKE credential

## Go to GCP project
```
PROJECT=[PROJECT_ID]
gcloud config set project ${PROJECT}
```
## Go to GKE cluster
```
gcloud container clusters get-credentials sample-cluster --zone ${REGION1}-c --project=${PROJECT}
kubectl config set-context --current --namespace=sample-app
```

