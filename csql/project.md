
# Create Project
Administrator will create project, assign billing account and add non-admin user as project owner.

Login with admin role.

## Enable policy
Allow all for org policy below
```
declare -a policies=("constraints/compute.trustedImageProjects" 
                "constraints/compute.vmExternalIpAccess"
                "constraints/compute.restrictSharedVpcSubnetworks"
                "constraints/compute.restrictSharedVpcHostProjects"
                "constraints/compute.restrictVpcPeering"
                "constraints/compute.vmCanIpForward"
                )
for policy in "${policies[@]}"
do
cat <<EOF > new_policy.yaml
constraint: $policy
listPolicy:
 allValues: ALLOW
EOF
gcloud resource-manager org-policies set-policy new_policy.yaml --project=${PROJECT}
done
```
### Disable enforce requireShieldedVm and requireOsLogin
```
gcloud resource-manager org-policies disable-enforce compute.requireShieldedVm --project=${PROJECT}
gcloud resource-manager org-policies disable-enforce compute.requireOsLogin --project=${PROJECT}
```

# Create VPC and enable IAP with non-admin role
Login with non-admin role.

## Create VPC and Firewall
### Set parameter
```
PROJECT=[PROJECT_ID]
REGION=us-west1
ZONE=us-west1-a
REGION2=us-central1
ZONE2=us-central1-c


gcloud config set project ${PROJECT}
```
### Create vpc
```
gcloud compute networks create devnet --project=${PROJECT} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
```
### Create subnet
```
gcloud compute networks subnets create ${REGION} --project=${PROJECT} --range=10.148.0.0/20 --network=devnet --region=${REGION}
gcloud compute networks subnets create ${REGION2} --project=${PROJECT} --range=10.146.0.0/20 --network=devnet --region=${REGION2}
```

### Create firewall for IAP
```
gcloud compute firewall-rules create allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT} \
  --network=devnet
```

## create virtual private access
```
gcloud compute addresses create google-managed-services-devnet \
--global \
--purpose=VPC_PEERING \
--prefix-length=16 \
--network=devnet \
--project=${PROJECT}

gcloud services vpc-peerings connect \
--service=servicenetworking.googleapis.com \
--ranges=google-managed-services-devnet \
--network=devnet \
--project=${PROJECT}

gcloud services vpc-peerings operations describe --name=operations/pssn.p24-284005574146-434f0348-1f6c-4a2b-947d-a5ab43f57b41
```

## create Cloud SQL instance 
```
gcloud sql instances create dbmysql \
--database-version=MYSQL_5_7 \
--cpu=1 \
--memory=3840MB \
--region=${REGION1} \
--no-assign-ip \
--network=${VPC1} \
--storage-type=SSD \
--storage-size=10GB
```

### Note Cloud SQL internal IP address
```
gcloud beta sql instances describe dbmysql | grep ipAddress:
```

## create client vm 
```
gcloud compute instances create pgdb-client --project=${PROJECT} --zone=${ZONE} --machine-type=e2-small --subnet=${REGION} --boot-disk-size=10GB --boot-disk-type=pd-balanced --tags=http-server
```

### Access Cloud SQL from VM
```
gcloud compute ssh --project=${PROJECT} --zone=${ZONE} pgdb-client --tunnel-through-iap
```
###  Inside VM
```
sudo apt-get update
sudo apt install telnet
telnet <csql-internal-ip> 3306
```
## Delete resources
```
gcloud sql instances delete dbmysql
gcloud compute instances delete pgdb-client --zone=${ZONE}
```


### Go back
[Content](https://github.com/adithaha/temp/blob/main/csql/readme.md)

