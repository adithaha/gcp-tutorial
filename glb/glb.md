# Create global load balancer

Create backend
```
gcloud compute backend-services create mig-lb-backend \
    --protocol=HTTP \
    --port-name=http \
    --health-checks=http-basic-check \
    --global
```

Add backend services
```
gcloud compute backend-services add-backend mig-lb-backend \
    --instance-group=mig-lb-instance-group \
    --instance-group-region=asia-southeast2 \
    --global
```

Create url-maps
```
gcloud compute url-maps create mig-lb-map-http --default-service mig-lb-backend
```

Create target proxies
```
gcloud compute target-http-proxies create mig-lb-proxy --url-map=mig-lb-map-http
```

Create forwarding rules
```
gcloud compute forwarding-rules create mig-lb-content-rule \
    --global \
    --target-http-proxy=mig-lb-proxy \
    --ports=80
```
