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
gcloud beta compute instances create ${VPC1}-spot --project=${PROJECT} --zone=${REGION1}-c --machine-type=e2-small --subnet=${VPC1}-${REGION1} --boot-disk-size=10GB --boot-disk-type=pd-balanced --provisioning-model=SPOT


gcloud compute instances create ${VPC1}-mongodb2 --project=${PROJECT} --zone=${REGION1}-c --machine-type=e2-standard-4 --subnet=${VPC1}-${REGION1} --boot-disk-size=1000GB --boot-disk-type=pd-ssd --image-family=centos-7 --image-project=centos-cloud

gcloud compute ssh --project=${PROJECT} --zone=${REGION1}-c ${VPC1}-mongodb2 --tunnel-through-iap

sudo yum install -y wget
sudo yum install -y java-1.8.0-openjdk-devel
sudo yum install -y maven

mkdir mongodb
cd ~/mongodb
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-4.4.11.tgz
tar xfvz mongodb-linux-x86_64-rhel70-4.4.11.tgz
mkdir /tmp/mongodb
cd mongodb-linux-x86_64-rhel70-4.4.11
./bin/mongod --dbpath /tmp/mongodb

cd ~/mongodb
curl -O --location https://github.com/brianfrankcooper/YCSB/releases/download/0.17.0/ycsb-0.17.0.tar.gz
tar xfvz ycsb-0.17.0.tar.gz

cd ~/mongodb

cd mongodb-linux-x86_64-rhel70-4.4.11
./bin/mongod --dbpath /tmp/mongodb
rm -rf /tmp/mongodb/*


cd ycsb-0.17.0
./bin/ycsb load mongodb -s -P workloads/small > outputLoad.txt

https://github.com/mongodb-developer/service-tests/blob/master/ycsb.md

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
