# Shutdown/remove all resources
## stop cluster
```
gcloud container clusters resize sample-cluster --node-pool default-pool --num-nodes 0 --zone asia-southeast2-c
gcloud compute instances stop gke-client --zone asia-southeast2-c
```
## start cluster
```
gcloud container clusters resize sample-cluster --node-pool default-pool --num-nodes 3 --zone asia-southeast2-c
gcloud compute instances start gke-client --zone asia-southeast2-c
```
## delete project
Delete GKE cluster
```
gcloud container clusters delete sample-cluster --zone "asia-southeast2-c" --project=${PROJECT}
```
Delete client VM
```
gcloud compute instances delete gke-client --zone asia-southeast2-c
```
Delete project
```
gcloud projects delete ${PROJECT}
```
