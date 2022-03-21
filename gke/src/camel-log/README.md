# camel-redis

## enable registry
```
gcloud services enable containerregistry.googleapis.com
```

## Build source
```
mvn clean package
```

## Build image
```
docker build -t asia.gcr.io/${PROJECT}/camel-log .
```

## Run image locally
```
docker run asia.gcr.io/gke-test-325904/camel-log
```

## Push image
```
docker push asia.gcr.io/${PROJECT}/camel-log
```
## create gke cluster
```
gcloud services enable container.googleapis.com --project=${PROJECT}
gcloud container clusters create sample-cluster --zone "${REGION1}-c" --machine-type "e2-medium" --release-channel "stable" --network ${VPC1} --subnetwork ${VPC1}-${REGION1} --num-nodes 3 --enable-shielded-nodes --project=${PROJECT} --scopes=https://www.googleapis.com/auth/devstorage.read_only 
gcloud container clusters get-credentials sample-cluster --zone "${REGION1}-c" --project=${PROJECT}

kubectl create namespace sample-app
kubectl config set-context --current --namespace=sample-app

```
## Deploy
```
kubectl apply -f k8s/deployment.yaml
kubectl describe deployment camel-log-deployment
kubectl get pods -l app=camel-log
POD_NAME=[POD_NAME]
```

```
gcloud container node-pools list --cluster sample-cluster --zone "${REGION1}-c" --project=${PROJECT}

gcloud container node-pools create pool2 --cluster sample-cluster --zone "${REGION1}-c" --project=${PROJECT} \
 --machine-type e2-medium \
 --num-nodes 3 \
 --scopes https://www.googleapis.com/auth/devstorage.read_only

```

Get logs and enter shell
```
kubectl logs -f ${POD_NAME}
kubectl exec -i -t ${POD_NAME} -- /bin/bash
```
