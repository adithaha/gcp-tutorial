# Create VPC and enable IAP
Login with non-admin role.

## Set parameter
[Parameter](https://github.com/adithaha/gcp-tutorial/blob/main/vpc/parameter.md)

## create vpc devnet1
```
gcloud compute networks create ${VPC1} --project=${PROJECT} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
gcloud compute networks subnets create ${VPC1}-${REGION1} --project=${PROJECT} --range=10.148.0.0/20 --network=${VPC1} --region=${REGION1}
gcloud compute networks subnets create ${VPC1}-${REGION2} --project=${PROJECT} --range=10.146.0.0/20 --network=${VPC1} --region=${REGION2}
```
## create vpc devnet2
```
gcloud compute networks create ${VPC2} --project=${PROJECT} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
gcloud compute networks subnets create ${VPC2}-${REGION1} --project=${PROJECT} --range=10.144.0.0/20 --network=${VPC2} --region=${REGION1}
gcloud compute networks subnets create ${VPC2}-${REGION2} --project=${PROJECT} --range=10.142.0.0/20 --network=${VPC2} --region=${REGION2}
```
## create firewall
```
gcloud compute firewall-rules create devnet1-allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT} \
  --network=${VPC1}
```

```
gcloud compute firewall-rules create devnet2-allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT} \
  --network=${VPC2}
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/vpc/readme.md)
