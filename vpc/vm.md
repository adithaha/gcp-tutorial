# Create VM
Login with non-admin role.
## Set parameter
```
PROJECT=[PROJECT_ID]
gcloud config set project ${PROJECT}

VPC1=devnet1
VPC2=devnet2

REGION1=asia-southeast2
REGION2=asia-southeast1
```
## create client vm 
```
gcloud compute instances create ${VPC1}-vm1 --project=${PROJECT} --zone=${REGION1}-c --machine-type=e2-small --subnet=${VPC1}-${REGION1} --boot-disk-size=10GB --boot-disk-type=pd-balanced
gcloud compute instances create ${VPC1}-vm2 --project=${PROJECT} --zone=${REGION2}-c --machine-type=e2-small --subnet=${VPC1}-${REGION2} --boot-disk-size=10GB --boot-disk-type=pd-balanced
gcloud compute instances create ${VPC2}-vm1 --project=${PROJECT} --zone=${REGION1}-c --machine-type=e2-small --subnet=${VPC2}-${REGION1} --boot-disk-size=10GB --boot-disk-type=pd-balanced
gcloud compute instances create ${VPC2}-vm2 --project=${PROJECT} --zone=${REGION2}-c --machine-type=e2-small --subnet=${VPC2}-${REGION2} --boot-disk-size=10GB --boot-disk-type=pd-balanced

```
