# Internal Load Balancer
Test internal ingress from client VM

## Acquire GKE access
[credential](https://github.com/adithaha/gcp-tutorial/blob/main/gke/credential.md)

# Create Client VM
```
gcloud services enable compute.googleapis.com --project=${PROJECT}
gcloud compute instances create gke-client --machine-type e2-medium --zone ${REGION1}-c --network ${VPC1} --subnet jakarta
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
gcloud compute ssh --project=${PROJECT} --zone=${REGION1}-c gke-client --tunnel-through-iap
```
Inside client VM, call Hello App using internal load balancer
```
curl <IP-INTERNAL-LB>:8080
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/readme.md)
