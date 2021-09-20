# Internal Load Balancer
Setup internal load balancer, and test from client VM



# Create Internal LB
```
kubectl config set-context --current --namespace=sample-app
kubectl apply -f ilb/service.yaml
```
Check load balancer IP in EXTERNAL-IP column
```
kubectl get svc hello-app-internal
```

# Create Client VM
```
PROJECT=[PROJECT_ID]
gcloud config set project ${PROJECT}
gcloud services enable compute.googleapis.com --project=${PROJECT}
gcloud compute instances create gke-client --machine-type e2-medium --zone asia-southeast2-c --network devnet --subnet jakarta
```
## create firewall to access client SSH
```
gcloud compute firewall-rules create allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT} \
  --network=devnet
```
# Call App from client VM
SSH into client VM
```
gcloud compute ssh --project=${PROJECT} --zone=asia-southeast2-c gke-client --tunnel-through-iap
```
Inside client VM, call Hello App using internal load balancer
```
curl <IP-INTERNAL-LB>:8080
```

### Go back
[Content](https://github.com/adithaha/temp/blob/main/gke/readme.md)
