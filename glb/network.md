# Network preparation

## Create subnet proxy
```
gcloud compute networks subnets create ${VPC1}-${REGION1}-proxy-only \
  --purpose=REGIONAL_MANAGED_PROXY \
  --role=ACTIVE \
  --region=${REGION1} \
  --network=${VPC1} \
  --range=10.129.0.0/23
```
```
gcloud compute networks subnets create ${VPC1}-${REGION2}-proxy-only \
  --purpose=REGIONAL_MANAGED_PROXY \
  --role=ACTIVE \
  --region=${REGION2} \
  --network=${VPC1} \
  --range=10.129.2.0/23
```

## Create firewall
Firewall for Regional LB
```
gcloud compute firewall-rules create fw-allow-proxies-${VPC1}-${REGION1} \
  --network=${VPC1} \
  --action=allow \
  --direction=ingress \
  --source-ranges=10.129.0.0/23 \
  --target-tags=load-balanced-backend \
  --rules=tcp:80,tcp:443,tcp:8080
gcloud compute firewall-rules create fw-allow-proxies-${VPC1}-${REGION2} \
  --network=${VPC1} \
  --action=allow \
  --direction=ingress \
  --source-ranges=10.129.2.0/23 \
  --target-tags=load-balanced-backend \
  --rules=tcp:80,tcp:443,tcp:8080
```
create firewall for healthcheck
```
gcloud compute firewall-rules create fw-allow-health-check-${VPC1} \
    --network=${VPC1} \
    --action=allow \
    --direction=ingress \
    --source-ranges=130.211.0.0/22,35.191.0.0/16 \
    --target-tags=fw-allow-health-check-${VPC1} \
    --rules=tcp:8080
```

## Create Healthcheck
Health check for managed instance group
```
gcloud compute health-checks create http http-basic-check \
  --port 8080 \
  --check-interval 10s \
  --unhealthy-threshold 3
```
Health check for regional LB
```
gcloud compute health-checks create http http-basic-check-${VPC1}-${REGION1} \
   --region=${REGION1} \
   --request-path='/' \
   --use-serving-port
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/glb/readme.md)
