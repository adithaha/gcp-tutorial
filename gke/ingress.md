
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
kubectl apply -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/service-nodeport.yaml
kubectl apply -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/service-nodeport1.yaml
kubectl apply -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/service-nodeport2.yaml
kubectl get svc
```
Create ingress
```
kubectl create -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/ingress.yaml
```
Create ingress internal
```
kubectl create -f https://raw.githubusercontent.com/adithaha/gcp-tutorial/main/gke/ingress/ingress-internal.yaml
```
## Access App using Ingress
Get LB address with command below, may need some time to get it
```
kubectl get ingress
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/readme.md)
