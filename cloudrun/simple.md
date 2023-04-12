## Deploy Hello App
## Create Cloud Run service 
```
gcloud services enable run.googleapis.com --project=${PROJECT}
gcloud run deploy hello-app --image=gcr.io/google-samples/hello-app:1.0 --project=${PROJECT} --region=${REGION1} --allow-unauthenticated
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/tree/main/cloudrun)
