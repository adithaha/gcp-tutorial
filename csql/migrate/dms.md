
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
--no-assign-ip \
--network=${VPC1} \
--storage-type=HDD \
--storage-size=20GB \
--enable-google-private-path
```
## create Cloud SQL instance destination
```
gcloud sql instances create dms-dest \
--database-version=MYSQL_8_0 \
--cpu=1 \
--memory=3840MB \
--project=${PROJECT} \
--region=${REGION1} \
--no-assign-ip \
--network=${VPC1} \
--storage-type=HDD \
--storage-size=20GB \
--enable-google-private-path
```

### Note Cloud SQL internal IP address
```
gcloud beta sql instances describe dms-src | grep ipAddress:
gcloud beta sql instances describe dms-dest | grep ipAddress:
```
```
DMS_SRC_IP=<dms-src-ip>
DMS_DEST_IP=<dms-dest-ip>
```
## create client vm 
```
gcloud compute instances create mydb-client --project=${PROJECT} --zone=${ZONE} --machine-type=e2-small --subnet=${REGION} --boot-disk-size=10GB --boot-disk-type=pd-balanced --tags=http-server

gcloud beta compute instances start devnet1-jkt-vm1-e2 --no-graceful-shutdown --zone=ZONE
```

### Access Cloud SQL from VM
```
gcloud compute ssh --project=${PROJECT} --zone=${ZONE} mydb-client --tunnel-through-iap
```
###  Inside VM
```
mkdir dms
cd dms
wget https://dev.mysql.com/get/mysql-apt-config_0.8.33-1_all.deb
sudo dpkg -i mysql-apt-config_0.8.33-1_all.deb
sudo apt-get update
sudo apt-get install telnet
sudo apt-get install zip
sudo apt-get install mysql-client
```

Try connecting mysql
```
mysql --ssl-mode=DISABLED --host=<dms-src-ip> --user=root
exit
```

```
wget https://github.com/datacharmer/test_db/archive/refs/tags/v1.0.7.zip
unzip test_db-master.zip
unzip v1.0.7.zip 
cd test_db-1.0.7/
mysql --ssl-mode=DISABLED --host=<dms-src-ip> --user=root -t < employees.sql
time mysql --ssl-mode=DISABLED --host=<dms-src-ip> --user=root -t < test_employees_sha.sql
```
## DMS
```
gcloud sql instances patch dms-src --backup-start-time=05:00
gcloud sql instances patch dms-src --enable-bin-log
gcloud sql instances patch dms-src --database-flags=general_log=on,slow_query_log=on,log_output=FILE

gcloud sql instances patch dms-dest --backup-start-time=05:00
gcloud sql instances patch dms-dest --enable-bin-log
gcloud sql instances patch dms-dest --database-flags=general_log=on,slow_query_log=on,log_output=FILE

```

```
#!/bin/bash
for i in $(seq 1 5000)
do
   echo "Welcome $i times"
   mysql --ssl-mode=DISABLED --host=<src-ip> --user=root -e "INSERT INTO employees.employees( emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES (111111$i,'1958-05-01','Sachin','Tsukuda','M','1997-11-30');"
done

mysql --ssl-mode=DISABLED --host=<src-ip> --user=root -e "select count(*) from employees.employees;"
```
## Delete resources
```
gcloud sql instances delete dms-src
gcloud sql instances delete dms-dest
gcloud beta compute instances stop devnet1-jkt-vm1-e2 --no-graceful-shutdown --zone=ZONE
```


### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/csql/readme.md)
