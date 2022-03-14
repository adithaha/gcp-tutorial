# camel-redis

## enable registry
gcloud services enable containerregistry.googleapis.com

## Build source
mvn clean package

## Build image
docker build -t asia.gcr.io/${PROJECT}/camel-log .

docker push asia.gcr.io/${PROJECT}/camel-log



