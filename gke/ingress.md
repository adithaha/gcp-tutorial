
# Setup access via Ingress

## Acquire GKE access
[credential](https://github.com/adithaha/gcp-tutorial/blob/main/gke/credential.md)

## Deploy second app
```
kubectl apply -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/deployment.yaml
```
## External Ingress
Create service nodeport
```
kubectl create -f kubectl apply -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/deployment.yaml
/ingress/service-nodeport.yaml
kubectl create -f kubectl apply -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/deployment.yaml
/ingress/service-nodeport1.yaml
kubectl create -f kubectl apply -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/deployment.yaml
/ingress/service-nodeport2.yaml
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

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/readme.md)
