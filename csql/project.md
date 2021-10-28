
# Create Project
Administrator will create project, assign billing account and add non-admin user as project owner.
Login with admin role.
## create project with billing account
```
gcloud projects create --name=gke-test
```
```
PROJECT=[PROJECT_ID]
BILLING_ACCOUNT=[BILLING_ACCOUNT]
```
```
gcloud beta billing projects link ${PROJECT} --billing-account ${BILLING_ACCOUNT}
```
```
gcloud projects add-iam-policy-binding ${PROJECT} --member=user:[USER_EMAIL] --role=roles/owner
```
## enable policy
Allow all for org policy below
```
compute.vmCanIpForward
compute.vmExternalIpAccess
```
Disable enforce requireShieldedVm and requireOsLogin
```
gcloud resource-manager org-policies disable-enforce compute.requireShieldedVm --project=${PROJECT}
gcloud resource-manager org-policies disable-enforce compute.requireOsLogin --project=${PROJECT}


gcloud resource-manager org-policies disable-enforce iam.allowedPolicyMemberDomains --project=${PROJECT}



gcloud org-policies describe iam.allowedPolicyMemberDomains --project=${PROJECT}
gcloud services enable orgpolicy.policy.get  --project=${PROJECT}
{"name":"projects/gke-test-325904/policies/iam.allowedPolicyMemberDomains","spec":{"rules": [{"allowAll":true}]}}

gcloud org-policies set-policy iam.json


```
# Create VPC and enable IAP
Login with non-admin role.
## Set parameter
```
PROJECT=[PROJECT_ID]
REGION=us-west1
ZONE=us-west1-a
REGION2=us-central1

gcloud config set project ${PROJECT}
```
## create vpc
```
gcloud compute networks create devnet --project=${PROJECT} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
gcloud compute networks subnets create ${REGION} --project=${PROJECT} --range=10.148.0.0/20 --network=devnet --region=${REGION}
gcloud compute networks subnets create ${REGION2} --project=${PROJECT} --range=10.146.0.0/20 --network=devnet --region=${REGION2}

```
## create firewall
```
gcloud compute firewall-rules create allow-ssh-ingress-from-iap \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:22 \
  --source-ranges=35.235.240.0/20 \
  --project=${PROJECT} \
  --network=devnet
```

### Go back
[Content](https://github.com/adithaha/temp/blob/main/csql/readme.md)


```
gcloud compute instances create pgdb-client --project=${PROJECT} --zone=${ZONE} --machine-type=e2-small --subnet=${REGION} --boot-disk-size=10GB --boot-disk-type=pd-balanced --no-address --tags=http-server
```
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



```
gcloud sql instances create myinstance \
--database-version=MYSQL_8_0 \
--cpu=2 \
--memory=7680MB \
--region=us-central1

gcloud beta sql instances create dbmysql \
--database-version=MYSQL_5_7 \
--cpu=1 \
--memory=3840MB \
--region=${REGION} \
--no-assign-ip \
--network=devnet \
--storage-type=SSD \
--storage-size=10GB

\
--gce-zone=GCE_ZONE \
--zone=ZONE
```
