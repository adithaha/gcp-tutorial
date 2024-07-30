
# Setup access via Ingress

## Acquire GKE access
[credential](https://github.com/adithaha/gcp-tutorial/blob/main/gke/credential.md)

## Deploy second app
```
kubectl apply -f ingress/deployment.yaml
```
## External Ingress
Create service nodeport
```
kubectl create -f ingress/service-nodeport.yaml
kubectl create -f ingress/service-nodeport1.yaml
kubectl create -f ingress/service-nodeport2.yaml
kubectl get svc
```
Create ingress
```
kubectl create -f ingress/ingress.yaml
```
## Access App using Ingress
Get LB address with command below, may need some time to get it
```
kubectl get ingress
```

