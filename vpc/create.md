# Create VPC and enable IAP
Login with non-admin role.
## Set parameter
```
PROJECT=[PROJECT_ID]
gcloud config set project ${PROJECT}
```
## create vpc devnet1
```
gcloud compute networks create devnet1 --project=${PROJECT} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
gcloud compute networks subnets create devnet1-jakarta --project=${PROJECT} --range=10.148.0.0/20 --network=devnet1 --region=asia-southeast2
gcloud compute networks subnets create devnet1-singapore --project=${PROJECT} --range=10.146.0.0/20 --network=devnet1 --region=asia-southeast1
```
## create vpc devnet2
```
gcloud compute networks create devnet2 --project=${PROJECT} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
gcloud compute networks subnets create devnet2-jakarta --project=${PROJECT} --range=10.144.0.0/20 --network=devnet2 --region=asia-southeast2
gcloud compute networks subnets create devnet2-singapore --project=${PROJECT} --range=10.142.0.0/20 --network=devnet2 --region=asia-southeast1
```
## create firewall
```
gcloud compute firewall-rules create devnet1-allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT} \
  --network=devnet1
```

```
gcloud compute firewall-rules create devnet2-allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT} \
  --network=devnet2
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/vpc/readme.md)