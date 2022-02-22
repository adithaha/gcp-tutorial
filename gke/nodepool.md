
# admin



## Nodepool
```
gcloud container node-pools create preemptible-pool \
    --cluster devnet1-sample-cluster \
    --num-nodes 3 \
    --zone asia-southeast2-c \
    --preemptible
```
```
gcloud container node-pools create spot-pool \
    --cluster devnet1-sample-cluster \
    --num-nodes 3 \
    --zone asia-southeast2-c \
    --spot
```

## External Ingress
Create nodeport
```
kubectl create -f service-nodeport.yaml
kubectl get svc
```

Create ingress
```
kubectl create -f ingress.yaml
kubectl get svc
```
```
gcloud compute firewall-rules create test-nodeport \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:31988,tcp:30743,tcp:30352 \
  --network=devnet

gcloud compute firewall-rules delete test-nodeport
```
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
## delete
```
gcloud container clusters delete sample-cluster --zone "asia-southeast2-c" --project=${PROJECT}
```
```
gcloud compute instances stop gke-client --zone asia-southeast2-c
```
```
gcloud projects delete ${PROJECT}
```
