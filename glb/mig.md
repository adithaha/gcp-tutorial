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
gcloud compute health-checks create http http-basic-check --port 8080 --check-interval 10s --unhealthy-threshold 3
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
    --size=2 --region=asia-southeast2 --health-check=http-basic-check --initial-delay=300
```

create named ports for instance group
```
gcloud compute instance-groups set-named-ports mig-lb-instance-group --named-ports http:8080 \
    --region asia-southeast2
```
