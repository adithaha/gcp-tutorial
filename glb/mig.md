# Deploy application in managed instance group

## create health check
create firewall for healthcheck
```
gcloud compute firewall-rules create fw-allow-health-check \
    --network=devnet \
    --action=allow \
    --direction=ingress \
    --source-ranges=130.211.0.0/22,35.191.0.0/16 \
    --target-tags=allow-health-check \
    --rules=tcp:8080
```

create health check
```
gcloud compute health-checks create http http-basic-check --port 8080
```

## create template
```
gcloud compute instance-templates create-with-container mig-lb-template --machine-type e2-medium \
    --subnet=jakarta --region=asia-southeast2 --boot-disk-size=10GB --boot-disk-type=pd-balanced \
    --no-address --tags=allow-health-check,http-server --container-image=gcr.io/google-samples/hello-app:1.0
```

## create instance group
Create instance group based on previous template
```
gcloud compute instance-groups managed create mig-lb-instance-group --template=mig-lb-template \
    --size=2 --region=asia-southeast2
```

create named ports for instance group
```
gcloud compute instance-groups set-named-ports mig-lb-instance-group --named-ports http:8080 \
    --region asia-southeast2
```


## Create global load balancer
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
