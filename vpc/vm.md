# Create VM
Login with non-admin role.

## Set parameter
[Parameter](https://github.com/adithaha/gcp-tutorial/blob/main/vpc/parameter.md)

## create client vm 
```
gcloud compute instances create ${VPC1}-vm1 --project=${PROJECT} --zone=${REGION1}-c --machine-type=e2-small --subnet=${VPC1}-${REGION1} --boot-disk-size=10GB --boot-disk-type=pd-balanced
gcloud compute instances create ${VPC1}-vm2 --project=${PROJECT} --zone=${REGION2}-c --machine-type=e2-small --subnet=${VPC1}-${REGION2} --boot-disk-size=10GB --boot-disk-type=pd-balanced
gcloud compute instances create ${VPC2}-vm1 --project=${PROJECT} --zone=${REGION1}-c --machine-type=e2-small --subnet=${VPC2}-${REGION1} --boot-disk-size=10GB --boot-disk-type=pd-balanced
gcloud compute instances create ${VPC2}-vm2 --project=${PROJECT} --zone=${REGION2}-c --machine-type=e2-small --subnet=${VPC2}-${REGION2} --boot-disk-size=10GB --boot-disk-type=pd-balanced

gcloud compute instances create ${VPC1}-pre --project=${PROJECT} --zone=${REGION1}-c --machine-type=e2-small --subnet=${VPC1}-${REGION1} --boot-disk-size=10GB --boot-disk-type=pd-balanced --preemptible

gcloud compute instances create ${VPC1}-spot --project=${PROJECT} --zone=${REGION1}-c --machine-type=e2-small --subnet=${VPC1}-${REGION1} --boot-disk-size=10GB --boot-disk-type=pd-balanced --spot


```

## Check connection 
```
gcloud compute ssh --project=${PROJECT} --zone=${REGION1}-c ${VPC1}-vm1 --tunnel-through-iap
```
Connect to ${VPC1}-vm2 with internal ip, should ok
```
ping 10.146.0.2
```
Connect to ${VPC2}-vm1 with internal ip, should fail
```
ping 10.144.0.2
```
Connect to ${VPC2}-vm2 with internal ip, should fail
```
ping 10.142.0.2
```


### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/vpc/readme.md)
