# camel-redis

## enable registry
gcloud services enable containerregistry.googleapis.com

## Build source
mvn clean package

## Build image
docker build -t asia.gcr.io/${PROJECT}/camel-log .

## Run image locally
docker run asia.gcr.io/gke-test-325904/camel-log

## Push image
docker push asia.gcr.io/${PROJECT}/camel-log



