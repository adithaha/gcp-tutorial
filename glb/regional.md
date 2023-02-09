# Create regional load balancer

Create proxy
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
```
gcloud compute firewall-rules create fw-allow-proxies-${VPC1}-${REGION1} \
  --network=${VPC1} \
  --action=allow \
  --direction=ingress \
  --source-ranges=10.129.0.0/23 \
  --target-tags=load-balanced-backend \
  --rules=tcp:80,tcp:443,tcp:8080
```
```
gcloud compute firewall-rules create fw-allow-proxies-${VPC1}-${REGION2} \
  --network=${VPC1} \
  --action=allow \
  --direction=ingress \
  --source-ranges=10.129.2.0/23 \
  --target-tags=load-balanced-backend \
  --rules=tcp:80,tcp:443,tcp:8080
```



```
gcloud compute health-checks create http http-basic-check-${VPC1}-${REGION1} \
   --region=${REGION1} \
   --request-path='/' \
   --use-serving-port
```
```
gcloud compute addresses create rlb-${VPC1}-${REGION1}  \
   --region=${REGION1} \
   --network-tier=STANDARD
```

```
gcloud compute addresses describe rlb-${VPC1}-${REGION1} --region=${REGION1}
```
Add backend services
```
gcloud compute backend-services create rlb-backend-${VPC1}-${REGION1} \
    --region=${REGION1} \
    --load-balancing-scheme=EXTERNAL_MANAGED \
    --protocol=HTTP \
    --port-name=http \
    --health-checks=http-basic-check-${VPC1}-${REGION1} \
    --health-checks-region=${REGION1}
    
gcloud compute backend-services add-backend rlb-backend-${VPC1}-${REGION1} \
    --balancing-mode=UTILIZATION \
    --instance-group=mig-lb-instance-group-${VPC1}-${REGION1} \
    --instance-group-region=${REGION1} \
    --region=${REGION1}
```

Create url-maps
```
gcloud compute url-maps create rlb-map-http-${VPC1}-${REGION1} \
    --default-service rlb-backend-${VPC1}-${REGION1} \
    --region=${REGION1}
```

Create target proxies
```
gcloud compute target-http-proxies create rlb-proxy-${VPC1}-${REGION1} \
    --url-map=rlb-map-http-${VPC1}-${REGION1} \
    --region=${REGION1}

```

Create forwarding rules
```
gcloud compute forwarding-rules create rlb-fw-rule-${VPC1}-${REGION1} \
    --load-balancing-scheme=EXTERNAL_MANAGED \
    --network-tier=STANDARD \
    --network=${VPC1} \
    --address=rlb-${VPC1}-${REGION1} \
    --ports=80 \
    --region=${REGION1} \
    --target-http-proxy=rlb-proxy-${VPC1}-${REGION1} \
    --target-http-proxy-region=${REGION1}
    

```

gcloud beta compute addresses describe rlb-${VPC1}-${REGION1} \
    --format="get(address)" \
    --region=${REGION1}

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/glb/readme.md)
