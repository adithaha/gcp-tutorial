## Create GKE cluster and deploy Hello App
## Create gke cluster
```
gcloud services enable container.googleapis.com --project=${PROJECT}
gcloud container clusters create sample-cluster --zone "${REGION1}-c" --machine-type "e2-medium" --release-channel "stable" --network ${VPC1} --subnetwork ${VPC1}-${REGION1} --num-nodes 3 --enable-shielded-nodes --project=${PROJECT} --scopes=https://www.googleapis.com/auth/devstorage.read_only 
```
## Get GKE credential
```
gcloud container clusters get-credentials sample-cluster --zone "${REGION1}-c" --project=${PROJECT}
```
## Set nodes number to 1
```
gcloud container clusters resize sample-cluster --node-pool default-pool \
    --num-nodes 1 \
    --zone ${REGION1}-c
```

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
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/readme.md)
