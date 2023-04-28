# Create global load balancer

Create backend
```
gcloud compute backend-services create mig-lb-backend-${VPC1}-${REGION1} \
    --protocol=HTTP \
    --port-name=http \
    --health-checks=http-basic-check \
    --global
```

Add backend services
```
gcloud compute backend-services add-backend mig-lb-backend-${VPC1}-${REGION1} \
    --instance-group=mig-lb-instance-group-${VPC1}-${REGION1} \
    --instance-group-region=${REGION1} \
    --global
```

Create url-maps
```
gcloud compute url-maps create glb-map-http-${VPC1}-${REGION1} \
    --default-service mig-lb-backend-${VPC1}-${REGION1}
```

Create target proxies
```
gcloud compute target-http-proxies create glb-proxy-${VPC1}-${REGION1} \
    --url-map=glb-map-http-${VPC1}-${REGION1}
```

Create forwarding rules
```
gcloud compute forwarding-rules create glb-fw-rule-${VPC1}-${REGION1} \
    --global \
    --target-http-proxy=glb-proxy-${VPC1}-${REGION1} \
    --ports=80
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/glb/readme.md)
