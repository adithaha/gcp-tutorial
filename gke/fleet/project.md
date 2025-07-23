# Create VPC and enable IAP
Login with non-admin role.

## Set parameter
- [Parameter](https://github.com/adithaha/gcp-tutorial/blob/main/common/parameter.md)
- [Parameter fleet](https://github.com/adithaha/gcp-tutorial/blob/main/gke/fleet/parameter.md)


## create vpc devnet1
```
gcloud compute networks create ${VPC1} --project=${PROJECT_HOST} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
gcloud compute networks subnets create ${VPC1}-${REGION1} --project=${PROJECT_HOST} --range=10.158.0.0/20 --network=${VPC1} --region=${REGION1}
gcloud compute networks subnets create ${VPC1}-${REGION2} --project=${PROJECT_HOST} --range=10.156.0.0/20 --network=${VPC1} --region=${REGION2}
```
## create vpc devnet2
```
gcloud compute networks create ${VPC2} --project=${PROJECT_HOST} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
gcloud compute networks subnets create ${VPC2}-${REGION1} --project=${PROJECT_HOST} --range=10.154.0.0/20 --network=${VPC2} --region=${REGION1}
gcloud compute networks subnets create ${VPC2}-${REGION2} --project=${PROJECT_HOST} --range=10.152.0.0/20 --network=${VPC2} --region=${REGION2}
```
## create firewall
```
gcloud compute firewall-rules create ${VPC1}-allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT_HOST} \
  --network=${VPC1}
  
gcloud compute firewall-rules create ${VPC1}-allow-internal \
  --direction=INGRESS \
  --action=allow \
  --rules=all \
  --priority=65534 \
  --source-ranges=10.128.0.0/9 \
  --project=${PROJECT_HOST} \
  --network=${VPC1}
```

```
gcloud compute firewall-rules create ${VPC2}-allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT_HOST} \
  --network=${VPC2}
  
gcloud compute firewall-rules create ${VPC2}-allow-internal \
  --direction=INGRESS \
  --action=allow \
  --rules=all \
  --priority=65534 \
  --source-ranges=10.128.0.0/9 \
  --project=${PROJECT_HOST} \
  --network=${VPC2}
```
## Policy
```
gcloud resource-manager org-policies disable-enforce compute.requireShieldedVm --project=${PROJECT_HOST}
gcloud resource-manager org-policies disable-enforce compute.requireOsLogin --project=${PROJECT_HOST}
gcloud resource-manager org-policies disable-enforce compute.trustedImageProjects --project=${PROJECT_HOST}
```

Allow all for org policy below
```
compute.vmCanIpForward
compute.vmExternalIpAccess
```
