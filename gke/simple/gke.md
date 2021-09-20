
# admin

## create project

```
gcloud projects create --name=gke-test

PROJECT=[PROJECT_ID]
BILLING_ACCOUNT=[BILLING_ACCOUNT]

gcloud beta billing projects link ${PROJECT} --billing-account ${BILLING_ACCOUNT}
```
```
gcloud projects add-iam-policy-binding ${PROJECT} --member=user:[USER_EMAIL] --role=roles/owner
```
## enable policy with gui
```
gcloud resource-manager org-policies disable-enforce compute.vmCanIpForward --project=${PROJECT}
gcloud resource-manager org-policies disable-enforce compute.vmExternalIpAccess --project=${PROJECT}
```
```
gcloud resource-manager org-policies disable-enforce compute.requireShieldedVm --project=${PROJECT}
gcloud resource-manager org-policies disable-enforce compute.requireOsLogin --project=${PROJECT}
```
# dev
```
PROJECT=[PROJECT_ID]
gcloud config set project ${PROJECT}

gcloud services enable compute.googleapis.com --project=${PROJECT}

gcloud compute instances create gke-client --machine-type e2-medium --zone asia-southeast2-c --network devnet --subnet jakarta
gcloud compute ssh --project=${PROJECT} --zone=asia-southeast2-c gke-client
```
## create vpc
```
gcloud compute networks create devnet --project=${PROJECT} --subnet-mode=custom --mtu=1460 --bgp-routing-mode=regional
gcloud compute networks subnets create jakarta --project=${PROJECT} --range=10.148.0.0/20 --network=devnet --region=asia-southeast2
gcloud compute networks subnets create singapore --project=${PROJECT} --range=10.146.0.0/20 --network=devnet --region=asia-southeast1
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

## create gke cluster
```
gcloud services enable container.googleapis.com --project=${PROJECT}
gcloud container clusters create sample-cluster --zone "asia-southeast2-c" --machine-type "e2-medium" --release-channel "stable" --network "devnet" --subnetwork "jakarta" --num-nodes 3 --enable-shielded-nodes --project=${PROJECT}
gcloud container clusters get-credentials sample-cluster --zone "asia-southeast2-c" --project=${PROJECT}
```

## deploy Hello App

Create namespace
```
kubectl create namespace sample-app
kubectl config set-context --current --namespace=sample-app
kubectl config view --minify | grep namespace:
```
Deploy Hello App
```
kubectl apply -f deployment.yaml
kubectl describe deployment hello-app-deployment
kubectl get pods -l app=hello-app
POD_NAME=[POD_NAME]
```
Get logs and enter shell
```
kubectl logs -f ${POD_NAME}
kubectl exec -i -t ${POD_NAME} -- /bin/bash
```
## External Load balancer
Create external lb
```
kubectl create service loadbalancer hello-app --tcp=8080:8080
kubectl get svc
```

## External Ingress
Create nodeport
```
kubectl create -f service-nodeport.yaml
kubectl get svc
```

Create ingress
```
kubectl create -f ingress.yaml
kubectl get svc
```
```
gcloud compute firewall-rules create test-nodeport \
  --direction=INGRESS \
  --action=allow \
  --rules=tcp:32127,tcp:31508 \
  --network=devnet
```
## stop cluster
```
gcloud container clusters resize sample-cluster --node-pool default-pool --num-nodes 0 --zone asia-southeast2-c
gcloud compute instances stop gke-client --zone asia-southeast2-c
```
## start cluster
```
gcloud container clusters resize sample-cluster --node-pool default-pool --num-nodes 3 --zone asia-southeast2-c
gcloud compute instances start gke-client --zone asia-southeast2-c
```
## delete
```
gcloud container clusters delete sample-cluster --zone "asia-southeast2-c" --project=${PROJECT}
```
```
gcloud compute instances stop gke-client --zone asia-southeast2-c
```
```
gcloud projects delete ${PROJECT}
```
