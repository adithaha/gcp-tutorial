## Create GKE cluster and deploy Hello App
## Create gke cluster 
```
gcloud services enable container.googleapis.com --project=${PROJECT-HOST}
gcloud services enable gkehub.googleapis.com --project=${PROJECT-HOST}

gcloud container fleet create --display-name=gke-fleet --project=${PROJECT-HOST}

gcloud container clusters create cluster-${PROJECT-HOST} --zone "${REGION1}-c" --machine-type "e2-medium" --release-channel "stable" --network ${VPC1} --subnetwork ${VPC1}-${REGION1} --num-nodes 1 --enable-shielded-nodes --project=${PROJECT-HOST} --scopes=https://www.googleapis.com/auth/devstorage.read_only --async

gcloud container clusters create cluster-${PROJECT-MEMBER} --zone "${REGION1}-c" --machine-type "e2-medium" --release-channel "stable" --network ${VPC1} --subnetwork ${VPC1}-${REGION1} --num-nodes 1 --enable-shielded-nodes --project=${PROJECT-MEMBER} --scopes=https://www.googleapis.com/auth/devstorage.read_only 
```
Check GKE cluster
```
gcloud container clusters list --project=${PROJECT-HOST}
gcloud container clusters list --project=${PROJECT-MEMBER}
```
## Get GKE credential
Fetch the credentials for cluster
```
gcloud container clusters get-credentials cluster-${PROJECT-HOST} --zone "${REGION1}-c" --project=${PROJECT-HOST}
gcloud container clusters get-credentials cluster-${PROJECT-MEMBER} --zone "${REGION1}-c" --project=${PROJECT-MEMBER}
```
Rename the cluster contexts so they are easier to reference later:
```
kubectl config rename-context gke_PROJECT_ID_ZONE_${PROJECT-HOST} ${PROJECT-HOST}
kubectl config rename-context gke_PROJECT_ID_ZONE_${PROJECT-MEMBER} ${PROJECT-MEMBER}
```
## Set nodes number to 1
```
cloud container clusters resize fleet-${PROJECT-HOST} --node-pool default-pool \
    --num-nodes 1
```



gcloud services enable gkehub.googleapis.com --project=${PROJECT-HOST}


## deploy Hello App
Create namespace
```
kubectl create namespace sample-app
kubectl config set-context --current --namespace=sample-app
kubectl config view --minify | grep namespace:
```
Deploy Hello App
```
kubectl apply -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/simple/deployment.yaml
kubectl describe deployment hello-app-deployment
kubectl get pods -l app=hello-app
POD_NAME=[POD_NAME]
```
Get logs and enter shell
```
kubectl logs -f ${POD_NAME}
kubectl exec -i -t ${POD_NAME} -- /bin/bash
```
## External Load balancer
Create external lb
```
kubectl create service loadbalancer hello-app --tcp=8080:8080
kubectl get svc
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/fleet/readme.md)
