## Deploy App
### Deploy app on cluster host
```
cat <<EOF > store-host-deployment.yaml
kind: Namespace
apiVersion: v1
metadata:
  name: store-host
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: store-host
  namespace: store-host
spec:
  replicas: 2
  selector:
    matchLabels:
      app: store-host
      version: v1
  template:
    metadata:
      labels:
        app: store-host
        version: v1
    spec:
      containers:
      - name: whereami
        image: gcr.io/google-samples/whereami:v1.2.1
        ports:
          - containerPort: 8080
EOF
kubectl apply -f store-host-deployment.yaml --context=cluster-${PROJECT_HOST}
```
### Deploy service on cluster host
```
cat <<EOF > store-host-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: store-host
  namespace: store-host
spec:
  selector:
    app: store-host
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store-host
  namespace: store-host
---
apiVersion: v1
kind: Service
metadata:
  name: store-host-service
  namespace: store-host
spec:
  selector:
    app: store-host
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store-host-service
  namespace: store-host
EOF

kubectl apply -f store-host-service.yaml --context=cluster-${PROJECT_HOST}
```
### Deploy app on cluster member
```
cat <<EOF > store-member-deployment.yaml
kind: Namespace
apiVersion: v1
metadata:
  name: store-member
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: store-member
  namespace: store-member
spec:
  replicas: 2
  selector:
    matchLabels:
      app: store-member
      version: v1
  template:
    metadata:
      labels:
        app: store-member
        version: v1
    spec:
      containers:
      - name: whereami
        image: gcr.io/google-samples/whereami:v1.2.1
        ports:
          - containerPort: 8080
EOF
kubectl apply -f store-member-deployment.yaml --context=cluster-${PROJECT_MEMBER}
```
### Deploy service on cluster member
```
cat <<EOF > store-member-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: store-member
  namespace: store-member
spec:
  selector:
    app: store-member
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store-member
  namespace: store-member
---
apiVersion: v1
kind: Service
metadata:
  name: store-member-service
  namespace: store-member
spec:
  selector:
    app: store-member
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store-member-service
  namespace: store-member
EOF
kubectl apply -f store-member-service.yaml --context=cluster-${PROJECT_MEMBER}
```

### Check Service

```
kubectl get serviceexports --context cluster-${PROJECT_HOST} --namespace store-host
kubectl get service --context cluster-${PROJECT_HOST} --namespace store-host
kubectl get endpoints --context cluster-${PROJECT_HOST} -n store-host
kubectl get serviceimport --context cluster-${PROJECT_HOST} -n store-host

kubectl get serviceexports --context cluster-${PROJECT_MEMBER} --namespace store-member
kubectl get service --context cluster-${PROJECT_MEMBER} --namespace store-member
kubectl get endpoints --context cluster-${PROJECT_MEMBER} -n store-member
kubectl get serviceimport --context cluster-${PROJECT_MEMBER} -n store-member

kubectl describe endpoints store-member --context cluster-${PROJECT_MEMBER} -n store-member



kubectl get serviceimport --context cluster-${PROJECT_MEMBER} -n store-member
NAME           TYPE           IP                   AGE
store          ClusterSetIP   ["34.118.237.205"]   36m
store-east-1   ClusterSetIP   ["34.118.234.121"]   36m
store-west-2   ClusterSetIP   ["34.118.227.80"]    16m

kubectl get endpoints store-member --context cluster-${PROJECT_MEMBER} -n store-member

```
## Check service
kubectl get pods -l app=store-host --namespace store-host --context cluster-${PROJECT_HOST}
kubectl exec -i -t store-host-5cfc9bf756-gbt2k --namespace store-host --context cluster-${PROJECT_HOST} -- /bin/bash

HOSTNAME.MEMBERSHIP_NAME.SERVICE_EXPORT_NAME.NAMESPACE.svc.clusterset.local


wget store-member
wget store-member-service

store          23m
store-east-1   23m

## Delete

## Delete

```
kubectl delete serviceexport store-host-service --context cluster-${PROJECT_HOST} -n store-host
kubectl delete service store-host-service --context cluster-${PROJECT_HOST} -n store-host
kubectl delete serviceexport store-host --context cluster-${PROJECT_HOST} -n store-host
kubectl delete service store-host --context cluster-${PROJECT_HOST} -n store-host
kubectl delete deployment store-host --context cluster-${PROJECT_HOST} -n store-host
kubectl delete namespace store-host --context cluster-${PROJECT_HOST}

kubectl delete serviceexport store-member-service --context cluster-${PROJECT_MEMBER} -n store-member
kubectl delete service store-member-service --context cluster-${PROJECT_MEMBER} -n store-member
kubectl delete serviceexport store-member --context cluster-${PROJECT_MEMBER} -n store-member
kubectl delete service store-member --context cluster-${PROJECT_MEMBER} -n store-member
kubectl delete deployment store-member --context cluster-${PROJECT_MEMBER} -n store-member
kubectl delete namespace store-member --context cluster-${PROJECT_MEMBER}
```


```

gcloud container fleet memberships unregister cluster-${PROJECT_MEMBER} \
  --gke-uri "https://container.googleapis.com/v1/projects/${PROJECT_MEMBER}/locations/${REGION1}-c/clusters/cluster-${PROJECT_MEMBER}" \
  --project=${PROJECT_HOST}
gcloud container fleet memberships unregister cluster-${PROJECT_HOST} \
  --gke-cluster ${REGION1}-c/cluster-${PROJECT_HOST} \
  --project=${PROJECT_HOST}

gcloud container clusters delete cluster-${PROJECT_MEMBER} \
  --zone "${REGION1}-c" \
  --project=${PROJECT_MEMBER}
gcloud container clusters delete cluster-${PROJECT_HOST} \
  --zone "${REGION1}-c" \
  --project=${PROJECT_HOST}
```
gcloud container fleet delete --project ${PROJECT_MEMBER}
```





### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/fleet/readme.md)
