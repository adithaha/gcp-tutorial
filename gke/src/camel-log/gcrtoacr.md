mvn clean package
docker build -t asia.gcr.io/${PROJECT}/camel-log .
docker push asia.gcr.io/${PROJECT}/camel-log

mvn clean package
docker build -t asia.gcr.io/${PROJECT}/camel-log-1 .
docker push asia.gcr.io/${PROJECT}/camel-log-1

mvn clean package
docker build -t asia.gcr.io/${PROJECT}/camel-log-2 .
docker push asia.gcr.io/${PROJECT}/camel-log-2

mvn clean package
docker build -t asia.gcr.io/${PROJECT}/camel-log-2:v1 .
docker push asia.gcr.io/${PROJECT}/camel-log-2:v1

mvn clean package
docker build -t asia.gcr.io/${PROJECT}/camel-log-2:v2 .
docker push asia.gcr.io/${PROJECT}/camel-log-2:v2

## test pulling image from gcr
### check local image
docker images

### remove local
docker rmi asia.gcr.io/${PROJECT}/camel-log-1
docker rmi asia.gcr.io/${PROJECT}/camel-log-2
docker rmi asia.gcr.io/${PROJECT}/camel-log-2:v1
docker rmi asia.gcr.io/${PROJECT}/camel-log-2:v2

### pull image from gcr
docker pull asia.gcr.io/${PROJECT}/camel-log-1
docker pull asia.gcr.io/${PROJECT}/camel-log-2
docker pull asia.gcr.io/${PROJECT}/camel-log-2:v1
docker pull asia.gcr.io/${PROJECT}/camel-log-2:v2

### check local image
docker images


gcloud container images list-gcr-usage --project=$PROJECT
gcloud projects add-iam-policy-binding $PROJECT --member='serviceAccount:service-$PROJECT_NUM@gcp-sa-artifactregistry.iam.gserviceaccount.com' --role='roles/storage.objectViewer'
gcloud projects add-iam-policy-binding $PROJECT --member='serviceAccount:service-$PROJECT_NUM@gcp-sa-artifactregistry.iam.gserviceaccount.com' --role='roles/storage.objectViewer'


gcloud artifacts docker upgrade migrate --projects=$PROJECT

roles/artifactregistry.containerRegistryMigrationAdmin

Cloud Asset Viewer (roles/cloudasset.viewer)
To analyze policies with custom IAM roles: Role Viewer (roles/iam.roleViewer)
To use the Google Cloud CLI to analyze policies: Service Usage Consumer (roles/serviceusage.serviceUsageConsumer)
