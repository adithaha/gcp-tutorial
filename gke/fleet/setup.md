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
 --scopes=gke-default,compute-ro,cloud-platform \
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
 --scopes=gke-default,compute-ro,cloud-platform 
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
## Enable Multi Cluster Service
Enable MCS
```
gcloud container fleet multi-cluster-services enable --project ${PROJECT_HOST}
```
IAM for MCS
```
 gcloud projects add-iam-policy-binding ${PROJECT_HOST} \
 --member "serviceAccount:${PROJECT_HOST}.svc.id.goog[gke-mcs/gke-mcs-importer]" \
 --role "roles/compute.networkViewer" \
 --project=${PROJECT_HOST}

gcloud projects add-iam-policy-binding ${PROJECT_HOST} \
    --member "principal://iam.googleapis.com/projects/${PROJECT_HOST_NUM}/locations/global/workloadIdentityPools/${PROJECT_HOST}.svc.id.goog/subject/ns/gke-mcs/sa/gke-mcs-importer" \
    --role "roles/compute.networkViewer"
```
Check MCS
```
gcloud container fleet multi-cluster-services describe --project=${PROJECT_HOST}
```




## Multi Cluster Gateway
```
gcloud container fleet ingress enable \
  --config-membership=cluster-${PROJECT_HOST} \
  --project=${PROJECT_HOST} \
  --location=${REGION1}

gcloud projects add-iam-policy-binding ${PROJECT_HOST} \
  --member "serviceAccount:service-${PROJECT_HOST_NUM}@gcp-sa-multiclusteringress.iam.gserviceaccount.com" \
  --role "roles/container.admin" \
  --project=${PROJECT_HOST}

gcloud container fleet ingress describe --project=${PROJECT_HOST}
kubectl get gatewayclasses --context=cluster-${PROJECT_HOST}
```



### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/fleet/readme.md)
