gcloud run services describe nodejs-http-function
gcloud run services describe nodejs-http-function --region=$REGION1
gcloud run services describe  function-1 --region=$REGION1
gcloud functions deploy func-gen1-instance   --gen1   --runtime nodejs16   --entry-point helloWorld   --source .   --region $REGION1   --trigger-http   --timeout 600s
  120  gcloud functions deploy func-gen1-instance   --runtime nodejs16   --entry-point helloWorld   --source .   --region $REGION1   --trigger-http   --timeout 600s
  121  gcloud functions deploy --help
  122  s
  123  ls
  124  
  
mkdir hello-http
cd hello-http/
{
  "name": "nodejs-functions-gen2-codelab",
  "version": "0.0.1",
  "main": "index.js",
  "dependencies": {
    "@google-cloud/functions-framework": "^2.0.0"
  }
}

Create cloud function gen 1
```
gcloud functions deploy func-gen1 \
  --no-gen2 \
  --no-allow-unauthenticated \
  --runtime nodejs21 \
  --entry-point helloWorld \
  --source . \
  --region asia-southeast2 \
  --trigger-http \
  --ingress-settings=internal-and-gclb \
  --timeout 300s
```

Create cloud function gen 2
```
gcloud functions deploy func-gen2-crun \
  --gen2 \
  --no-allow-unauthenticated \
  --runtime nodejs20 \
  --entry-point helloWorld \
  --source . \
  --region asia-southeast2 \
  --trigger-http \
  --timeout 600s
```
Create cloud run function
```
gcloud run deploy func-crun \
  --source . \
  --function helloWorld \
  --base-image nodejs20 \
  --no-allow-unauthenticated \
  --region asia-southeast2
```
detach cloud function gen 2
```
gcloud beta functions detach
```
