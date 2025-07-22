## Create GKE cluster and deploy Hello App
## Create gke cluster 
Enable GKE and GKE HUB API
```
gcloud services enable container.googleapis.com --project=${PROJECT_HOST}
gcloud services enable gkehub.googleapis.com --project=${PROJECT_HOST}
```
Create GKE Fleet
```
gcloud container fleet create --display-name=gke-fleet --project=${PROJECT_HOST}
```
Create GKE Cluster
```
gcloud container clusters create cluster-${PROJECT_HOST} --zone "${REGION1}-c" --machine-type "e2-medium" --release-channel "stable" --network ${VPC1} --subnetwork ${VPC1}-${REGION1} --num-nodes 1 --enable-shielded-nodes --project=${PROJECT_HOST} --scopes=https://www.googleapis.com/auth/devstorage.read_only --async

gcloud container clusters create cluster-${PROJECT_MEMBER} --zone "${REGION1}-c" --machine-type "e2-medium" --release-channel "stable" --network ${VPC1} --subnetwork ${VPC1}-${REGION1} --num-nodes 1 --enable-shielded-nodes --project=${PROJECT_MEMBER} --scopes=https://www.googleapis.com/auth/devstorage.read_only 
```
Check GKE cluster
```
gcloud container clusters list --project=${PROJECT_HOST}
gcloud container clusters list --project=${PROJECT_MEMBER}
```
## Get GKE credential
Fetch the credentials for cluster
```
gcloud container clusters get-credentials cluster-${PROJECT_HOST} --zone "${REGION1}-c" --project=${PROJECT-HOST}
gcloud container clusters get-credentials cluster-${PROJECT_MEMBER} --zone "${REGION1}-c" --project=${PROJECT-MEMBER}
```
Rename the cluster contexts so they are easier to reference later:
```
kubectl config rename-context gke_PROJECT_ID_ZONE_${PROJECT_HOST} ${PROJECT_HOST}
kubectl config rename-context gke_PROJECT_ID_ZONE_${PROJECT_MEMBER} ${PROJECT_MEMBER}
```


## Register clusters to the fleet
Enable the multi-cluster Gateway API on the cluster1 cluster
```
gcloud container clusters update cluster-${PROJECT_HOST}  --gateway-api=standard --region="${REGION1}-c"
```
Register the clusters to a fleet:
```
gcloud container fleet memberships register cluster-${PROJECT_HOST} \
  --gke-cluster ${REGION1}-c/cluster-${PROJECT_HOST} \
  --enable-workload-identity \
  --project=${PROJECT_HOST}

gcloud container fleet memberships register cluster-${PROJECT_MEMBER} \
  --gke-cluster ${REGION1}-c/cluster-${PROJECT_MEMBER} \
  --enable-workload-identity \
  --project=${PROJECT_MEMBER}
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
