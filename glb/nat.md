# create NAT

## Set project
```
PROJECT=[PROJECT_ID]
gcloud config set project ${PROJECT}
```

## Create NAT
Create NAT in network devnet asia-southeast2
```
gcloud compute routers create nat-router --network devnet --region asia-southeast2
gcloud compute routers nats create nat-config --router-region asia-southeast2 --router nat-router --nat-all-subnet-ip-ranges --auto-allocate-nat-external-ips
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/glb/readme.md)
