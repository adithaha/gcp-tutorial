# Create VM
Login with non-admin role.

## Set parameter
[Parameter](https://github.com/adithaha/gcp-tutorial/blob/main/common/parameter.md)

## create client vm 
```
gcloud compute instances create ${VPC1}-mongodb-n2 --project=${PROJECT} --zone=${REGION1}-c --machine-type=n2-standard-4 --subnet=${VPC1}-${REGION1} --boot-disk-size=1000GB --boot-disk-type=pd-ssd --image-family=centos-7 --image-project=centos-cloud
```

## Install MongoDB
SSH into machine 
```
gcloud compute ssh --project=${PROJECT} --zone=${REGION1}-c ${VPC1}-mongodb-n2 --tunnel-through-iap
```
Install MongoDB
```
sudo yum install -y wget
sudo yum install -y java-1.8.0-openjdk-devel
sudo yum install -y maven

mkdir mongodb
cd ~/mongodb
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-4.4.11.tgz
tar xfvz mongodb-linux-x86_64-rhel70-4.4.11.tgz
mkdir /tmp/mongodb
```
Run MongoDB
```
cd mongodb-linux-x86_64-rhel70-4.4.11
./bin/mongod --dbpath /tmp/mongodb
```
Clean MongoDB data
```
rm -rf /tmp/mongodb/*
```

## Install YCSB
Open new window
SSH into machine 
```
gcloud compute ssh --project=${PROJECT} --zone=${REGION1}-c ${VPC1}-mongodb-n2 --tunnel-through-iap
```
Install YCSB
```
cd ~/mongodb
curl -O --location https://github.com/brianfrankcooper/YCSB/releases/download/0.17.0/ycsb-0.17.0.tar.gz
tar xfvz ycsb-0.17.0.tar.gz
```
Config load parameter
```
cd ~/mongodb/ycsb-0.17.0
vi workloads/small
```
small content file
```
requestdistribution=zipfian
recordcount=4096000
operationcount=20000000
workload=site.ycsb.workloads.CoreWorkload
readallfields=true
readproportion=0.5
updateproportion=0.5
scanproportion=0
insertproportion=0.0
requestdistribution=zipfian
fieldcount=25
```
Run YCSB - load data
```
./bin/ycsb load mongodb -s -P workloads/small > outputLoad.txt
```
Run YCSB - run data
```
./bin/ycsb run mongodb -s -P workloads/small > outputLoad.txt
```
## Install FIO
Open new window
SSH into machine 
```
gcloud compute ssh --project=${PROJECT} --zone=${REGION1}-c ${VPC1}-mongodb-n2 --tunnel-through-iap
```
Install FIO 
```
sudo yum install epel-release -y
sudo yum install fio -y
```
Run FIO 
```
fio --randrepeat=1 --ioengine=libaio --direct=1 --gtod_reduce=1 --name=fiotest --filename=testfio --bs=4k --iodepth=64 --size=8G --readwrite=randrw --rwmixread=75
```


### Source
[YCSB](https://github.com/mongodb-developer/service-tests/blob/master/ycsb.md)
