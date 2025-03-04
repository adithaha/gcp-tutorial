

```
### Create vpc
```
gcloud config set project ${PROJECT}
```
## create virtual private access
```
gcloud compute addresses create google-managed-services-devnet1 \
--global \
--purpose=VPC_PEERING \
--prefix-length=16 \
--network=devnet \
--project=${PROJECT}

gcloud services vpc-peerings connect \
--service=servicenetworking.googleapis.com \
--ranges=google-managed-services-devnet1 \
--network=devnet1 \
--project=${PROJECT}

gcloud services vpc-peerings update \
--network=${VPC1} \
--ranges=google-managed-services-${VPC1} \
--service=servicenetworking.googleapis.com \
--force

gcloud services vpc-peerings operations describe --name=operations/pssn.p24-284005574146-434f0348-1f6c-4a2b-947d-a5ab43f57b41
```

## create Cloud SQL instance source
```
gcloud sql instances create dms-src \
--database-version=MYSQL_8_0 \
--cpu=1 --memory=3840MB \
--project=${PROJECT} \
--region=${REGION1} \
--enable-private-service-connect \
--allowed-psc-projects=${PROJECT} \
--no-assign-ip \
--network=${VPC1} \
--storage-type=HDD \
--storage-size=100GB
```
## create Cloud SQL instance destination
```
gcloud sql instances create dms-dest \
--database-version=MYSQL_8_0 \
--cpu=1 \
--memory=3840MB \
--project=${PROJECT} \
--region=${REGION1} \
--enable-private-service-connect \
--allowed-psc-projects=${PROJECT} \
--no-assign-ip \
--network=${VPC1} \
--storage-type=HDD \
--storage-size=100GB
```

### Note Cloud SQL internal IP address
```
gcloud beta sql instances describe dbmysql | grep ipAddress:
```

## create client vm 
```
gcloud compute instances create mydb-client --project=${PROJECT} --zone=${ZONE} --machine-type=e2-small --subnet=${REGION} --boot-disk-size=10GB --boot-disk-type=pd-balanced --tags=http-server
```

### Access Cloud SQL from VM
```
gcloud compute ssh --project=${PROJECT} --zone=${ZONE} mydb-client --tunnel-through-iap
```
###  Inside VM
```
sudo apt-get update
sudo apt install telnet
telnet <csql-internal-ip> 3306
```
## Delete resources
```
gcloud sql instances delete dms-src
gcloud compute instances delete mydb-client --zone=${ZONE}
```


### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/csql/readme.md)
