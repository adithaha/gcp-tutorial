# create NAT

## Set project
```
PROJECT=[PROJECT_ID]
gcloud config set project ${PROJECT}
```

## Create NAT
Create NAT in network VPC1 REGION1
```
gcloud compute routers create nat-router-${VPC1}-${REGION1} --network ${VPC1} --region ${REGION1}
gcloud compute routers nats create nat-config-${VPC1}-${REGION1} --router-region ${REGION1} --router nat-router-${VPC1}-${REGION1} --nat-all-subnet-ip-ranges --auto-allocate-nat-external-ips
```

Create NAT in network VPC1 REGION2
```
gcloud compute routers create nat-router-${VPC1}-${REGION2} --network ${VPC1} --region ${REGION2}
gcloud compute routers nats create nat-config-${VPC1}-${REGION2} --router-region ${REGION2} --router nat-router-${VPC1}-${REGION2} --nat-all-subnet-ip-ranges --auto-allocate-nat-external-ips
```
