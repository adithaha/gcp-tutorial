# Deploy application in managed instance group

## create health check
create firewall for healthcheck
```
gcloud compute firewall-rules create fw-allow-health-check-${VPC1} \
    --network=${VPC1} \
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
With VPC1 REGION1
```
gcloud compute instance-templates create-with-container mig-lb-template-${VPC1}-${REGION1} --machine-type e2-small \
    --subnet=${VPC1}-${REGION1} --region=${REGION1} --boot-disk-size=10GB --boot-disk-type=pd-balanced \
    --no-address --tags=fw-allow-health-check-${VPC1},http-server --container-image=gcr.io/google-samples/hello-app:1.0
```

With VPC1 REGION2
```
gcloud compute instance-templates create-with-container mig-lb-template-${VPC1}-${REGION2} --machine-type e2-small \
    --subnet=${VPC1}-${REGION2} --region=${REGION2} --boot-disk-size=10GB --boot-disk-type=pd-balanced \
    --no-address --tags=fw-allow-health-check-${VPC1},http-server --container-image=gcr.io/google-samples/hello-app:1.0
```

## create instance group
Create instance group based on previous template

With VPC1 REGION1
```
gcloud compute instance-groups managed create mig-lb-instance-group-${VPC1}-${REGION1} --template=mig-lb-template-${VPC1}-${REGION1} \
    --size=2 --region=${REGION1} --health-check=http-basic-check --initial-delay=300
```

With VPC1 REGION2
```
gcloud compute instance-groups managed create mig-lb-instance-group-${VPC1}-${REGION2} --template=mig-lb-template-${VPC1}-${REGION2} \
    --size=2 --region=${REGION2} --health-check=http-basic-check --initial-delay=300
```


create named ports for instance group

With VPC1 REGION1
```
gcloud compute instance-groups set-named-ports mig-lb-instance-group-${VPC1}-${REGION1} --named-ports http:8080 \
    --region ${REGION1}
```

With VPC1 REGION2
```
gcloud compute instance-groups set-named-ports mig-lb-instance-group-${VPC1}-${REGION2} --named-ports http:8080 \
    --region ${REGION2}
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/glb/readme.md)
