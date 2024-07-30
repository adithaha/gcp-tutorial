# Shutdown/remove all resources

### Acquire GKE credential
[credential](https://github.com/adithaha/gcp-tutorial/blob/main/gke/credential.md)


## stop cluster
```
gcloud container clusters resize sample-cluster --node-pool default-pool --num-nodes 0 --zone ${REGION1}-c
gcloud compute instances stop gke-client --zone ${REGION1}-c
```
## start cluster
```
gcloud container clusters resize sample-cluster --node-pool default-pool --num-nodes 3 --zone ${REGION1}-c
gcloud compute instances start gke-client --zone ${REGION1}-c
```
## delete project
Delete GKE cluster
```
gcloud container clusters delete sample-cluster --zone "${REGION1}-c" --project=${PROJECT}
```
Delete client VM
```
gcloud compute instances delete gke-client --zone ${REGION1}-c
```
Delete project
```
gcloud projects delete ${PROJECT}
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/readme.md)
