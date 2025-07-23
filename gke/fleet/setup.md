## Create GKE cluster and deploy Hello App

## IAM
```
gcloud beta services identity create --service=gkehub.googleapis.com --project=${PROJECT_HOST}

gcloud projects add-iam-policy-binding "${PROJECT_HOST}" \
  --member "serviceAccount:service-${PROJECT_HOST_NUM}@gcp-sa-gkehub.iam.gserviceaccount.com" \
  --role roles/gkehub.serviceAgent
gcloud projects add-iam-policy-binding "${PROJECT_MEMBER}" \
  --member "serviceAccount:service-${PROJECT_HOST_NUM}@gcp-sa-gkehub.iam.gserviceaccount.com" \
  --role roles/gkehub.serviceAgent
gcloud projects add-iam-policy-binding "${PROJECT_MEMBER}" \
  --member "serviceAccount:service-${PROJECT_HOST_NUM}@gcp-sa-gkehub.iam.gserviceaccount.com" \
  --role roles/gkehub.crossProjectServiceAgent
```
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
gcloud container clusters create cluster-${PROJECT_HOST} \
 --zone "${REGION1}-c" \
 --machine-type "e2-medium" \
 --release-channel "stable" \
 --workload-pool=${PROJECT_HOST}.svc.id.goog \
 --network ${VPC1} \
 --subnetwork ${VPC1}-${REGION1} \
 --num-nodes 1 \
 --enable-shielded-nodes \
 --project=${PROJECT_HOST} \
 --scopes=https://www.googleapis.com/auth/devstorage.read_only \
 --async

gcloud container clusters create cluster-${PROJECT_MEMBER} \
 --zone "${REGION1}-c" \
 --machine-type "e2-medium" \
 --release-channel "stable" \
 --workload-pool=${PROJECT_MEMBER}.svc.id.goog \
 --network ${VPC1} \
 --subnetwork ${VPC1}-${REGION1} \
 --num-nodes 1 \
 --enable-shielded-nodes \
 --project=${PROJECT_MEMBER} \
 --scopes=https://www.googleapis.com/auth/devstorage.read_only 
```
Check GKE cluster
```
gcloud container clusters list --project=${PROJECT_HOST}
gcloud container clusters list --project=${PROJECT_MEMBER} --uri
```
## Get GKE credential
Fetch the credentials for cluster
```
gcloud container clusters get-credentials cluster-${PROJECT_HOST} \
 --zone "${REGION1}-c" --project=${PROJECT_HOST}

gcloud container clusters get-credentials cluster-${PROJECT_MEMBER} \
 --zone "${REGION1}-c" --project=${PROJECT_MEMBER}
```
Rename the cluster contexts so they are easier to reference later:
```
kubectl config rename-context gke_${PROJECT_HOST}_${REGION1}-c_cluster-${PROJECT_HOST} cluster-${PROJECT_HOST}
kubectl config rename-context gke_${PROJECT_MEMBER}_${REGION1}-c_cluster-${PROJECT_MEMBER} cluster-${PROJECT_MEMBER}
```


## Register clusters to the fleet
Enable the multi-cluster Gateway API on the cluster1 cluster
```
gcloud container clusters update cluster-${PROJECT_HOST}  --gateway-api=standard --region="${REGION1}-c"
```
Use Workload pool if not yet:
```
gcloud container clusters update cluster-${PROJECT_HOST} \
    --location=${REGION1}-c \
    --workload-pool=${PROJECT_HOST}.svc.id.goog

gcloud container clusters update cluster-${PROJECT_MEMBER} \
    --location=${REGION1}-c \
    --workload-pool=${PROJECT_MEMBER}.svc.id.goog \
    --project=${PROJECT_MEMBER}
```
Register the clusters to a fleet:
```
gcloud container fleet memberships register cluster-${PROJECT_HOST} \
  --gke-cluster ${REGION1}-c/cluster-${PROJECT_HOST} \
  --enable-workload-identity \
  --project=${PROJECT_HOST}

gcloud container fleet memberships register cluster-${PROJECT_MEMBER} \
  --gke-uri "https://container.googleapis.com/v1/projects/${PROJECT_MEMBER}/locations/${REGION1}-c/clusters/cluster-${PROJECT_MEMBER}" \
  --enable-workload-identity \
  --project=${PROJECT_HOST}
```
Verify Fleet
```
gcloud container fleet memberships list --project=${PROJECT_HOST}
```
## MCS
```
gcloud container fleet multi-cluster-services enable --project ${PROJECT_HOST}
```
```
 gcloud projects add-iam-policy-binding ${PROJECT_HOST} \
 --member "serviceAccount:${PROJECT_HOST}.svc.id.goog[gke-mcs/gke-mcs-importer]" \
 --role "roles/compute.networkViewer" \
 --project=${PROJECT_HOST}
```
```
gcloud container fleet multi-cluster-services describe --project=${PROJECT_HOST}



gcloud container fleet ingress enable \
  --config-membership=cluster-${PROJECT_HOST} \
  --project=${PROJECT_HOST} \
  --location=${REGION1}

gcloud container fleet ingress describe --project=${PROJECT_HOST}

gcloud projects add-iam-policy-binding ${PROJECT_HOST} \
  --member "serviceAccount:service-${PROJECT_HOST_NUM}@gcp-sa-multiclusteringress.iam.gserviceaccount.com" \
  --role "roles/container.admin" \
  --project=${PROJECT_HOST}


kubectl get gatewayclasses --context=cluster-${PROJECT_HOST}
```

## Deploy App

```
cat <<EOF > store-deployment.yaml
kind: Namespace
apiVersion: v1
metadata:
  name: store
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: store
  namespace: store
spec:
  replicas: 2
  selector:
    matchLabels:
      app: store
      version: v1
  template:
    metadata:
      labels:
        app: store
        version: v1
    spec:
      containers:
      - name: whereami
        image: gcr.io/google-samples/whereami:v1.2.1
        ports:
          - containerPort: 8080
EOF
kubectl apply -f store-deployment.yaml --context=cluster-${PROJECT_MEMBER}
```
```
cat <<EOF > store-host-deployment.yaml
kind: Namespace
apiVersion: v1
metadata:
  name: store-host
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: store-host
  namespace: store-host
spec:
  replicas: 2
  selector:
    matchLabels:
      app: store-host
      version: v1
  template:
    metadata:
      labels:
        app: store-host
        version: v1
    spec:
      containers:
      - name: whereami
        image: gcr.io/google-samples/whereami:v1.2.1
        ports:
          - containerPort: 8080
EOF
kubectl apply -f store-host-deployment.yaml --context=cluster-${PROJECT_HOST}
```
```
cat <<EOF > store-east-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: store
  namespace: store
spec:
  selector:
    app: store
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store
  namespace: store
---
apiVersion: v1
kind: Service
metadata:
  name: store-east-1
  namespace: store
spec:
  selector:
    app: store
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store-east-1
  namespace: store
EOF

kubectl apply -f store-east-service.yaml --context=cluster-${PROJECT_MEMBER}
```
```
cat <<EOF > store-west-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: store
  namespace: store
spec:
  selector:
    app: store
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store
  namespace: store
---
apiVersion: v1
kind: Service
metadata:
  name: store-west-2
  namespace: store
spec:
  selector:
    app: store
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store-west-2
  namespace: store
EOF
kubectl apply -f store-west-service.yaml --context=cluster-${PROJECT_HOST}
```
```
kubectl get serviceexports --context cluster-${PROJECT_HOST} --namespace store
kubectl get serviceexports --context cluster-${PROJECT_MEMBER} --namespace store


kubectl get service --context cluster-${PROJECT_MEMBER} --namespace store
store          23m
store-east-1   23m

kubectl get endpoints --context cluster-${PROJECT_MEMBER} -n store

kubectl get serviceimport --context cluster-${PROJECT_MEMBER} -n store
NAME           TYPE           IP                   AGE
store          ClusterSetIP   ["34.118.237.205"]   36m
store-east-1   ClusterSetIP   ["34.118.234.121"]   36m
store-west-2   ClusterSetIP   ["34.118.227.80"]    16m



service/store created
serviceexport.net.gke.io/store created
service/store-west-2 created
serviceexport.net.gke.io/store-west-2 created
```
## Check service
kubectl describe deployment hello-app-deployment
kubectl get pods -l app=store --namespace store --context cluster-${PROJECT_HOST}
kubectl exec -i -t store-679dbc87d5-cz26h --namespace store --context cluster-${PROJECT_HOST} -- /bin/bash

wget store-west-1
wget store-east-1

store          23m
store-east-1   23m

## Delete

## Delete

```
kubectl delete serviceexport store-west-2 --context cluster-${PROJECT_HOST} -n store
kubectl delete service store-west-2 --context cluster-${PROJECT_HOST} -n store
kubectl delete serviceexport store --context cluster-${PROJECT_HOST} -n store
kubectl delete service store --context cluster-${PROJECT_HOST} -n store
kubectl delete deployment store --context cluster-${PROJECT_HOST} -n store
kubectl delete namespace store --context cluster-${PROJECT_HOST}

kubectl delete serviceexport store-east-1 --context cluster-${PROJECT_MEMBER} -n store
kubectl delete service store-east-1 --context cluster-${PROJECT_MEMBER} -n store
kubectl delete serviceexport store --context cluster-${PROJECT_MEMBER} -n store
kubectl delete service store --context cluster-${PROJECT_MEMBER} -n store
kubectl delete deployment store --context cluster-${PROJECT_MEMBER} -n store
kubectl delete namespace store --context cluster-${PROJECT_MEMBER}
```


```
gcloud container fleet memberships unregister cluster-${PROJECT_MEMBER} \
  --gke-cluster ${REGION1}-c/cluster-${PROJECT_MEMBER} \
  --project=${PROJECT_MEMBER}
```
```
gcloud container fleet delete --project ${PROJECT_MEMBER}
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
