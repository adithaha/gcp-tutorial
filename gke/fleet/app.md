

## Deploy App
Deploy app on cluster host

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

kubectl apply -f store-host-service.yaml --context=cluster-${PROJECT_MEMBER}
```

Deploy app on cluster member
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
```
cat <<EOF > store-west-service.yaml
apiVersion: v1
kind: Service
metadata:
  name: store
  namespace: store
spec:
  selector:
    app: store
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store
  namespace: store
---
apiVersion: v1
kind: Service
metadata:
  name: store-west-2
  namespace: store
spec:
  selector:
    app: store
  ports:
  - port: 8080
    targetPort: 8080
---
kind: ServiceExport
apiVersion: net.gke.io/v1
metadata:
  name: store-west-2
  namespace: store
EOF
kubectl apply -f store-west-service.yaml --context=cluster-${PROJECT_HOST}
```
```
kubectl get serviceexports --context cluster-${PROJECT_HOST} --namespace store
kubectl get serviceexports --context cluster-${PROJECT_MEMBER} --namespace store


kubectl get service --context cluster-${PROJECT_MEMBER} --namespace store
store          23m
store-east-1   23m

kubectl get endpoints --context cluster-${PROJECT_MEMBER} -n store

kubectl get serviceimport --context cluster-${PROJECT_MEMBER} -n store
NAME           TYPE           IP                   AGE
store          ClusterSetIP   ["34.118.237.205"]   36m
store-east-1   ClusterSetIP   ["34.118.234.121"]   36m
store-west-2   ClusterSetIP   ["34.118.227.80"]    16m



service/store created
serviceexport.net.gke.io/store created
service/store-west-2 created
serviceexport.net.gke.io/store-west-2 created
```
## Check service
kubectl describe deployment hello-app-deployment
kubectl get pods -l app=store --namespace store --context cluster-${PROJECT_HOST}
kubectl exec -i -t store-679dbc87d5-cz26h --namespace store --context cluster-${PROJECT_HOST} -- /bin/bash

wget store-west-1
wget store-east-1

store          23m
store-east-1   23m

## Delete

## Delete

```
kubectl delete serviceexport store-west-2 --context cluster-${PROJECT_HOST} -n store
kubectl delete service store-west-2 --context cluster-${PROJECT_HOST} -n store
kubectl delete serviceexport store --context cluster-${PROJECT_HOST} -n store
kubectl delete service store --context cluster-${PROJECT_HOST} -n store
kubectl delete deployment store --context cluster-${PROJECT_HOST} -n store
kubectl delete namespace store --context cluster-${PROJECT_HOST}

kubectl delete serviceexport store-east-1 --context cluster-${PROJECT_MEMBER} -n store
kubectl delete service store-east-1 --context cluster-${PROJECT_MEMBER} -n store
kubectl delete serviceexport store --context cluster-${PROJECT_MEMBER} -n store
kubectl delete service store --context cluster-${PROJECT_MEMBER} -n store
kubectl delete deployment store --context cluster-${PROJECT_MEMBER} -n store
kubectl delete namespace store --context cluster-${PROJECT_MEMBER}
```


```
gcloud container fleet memberships unregister cluster-${PROJECT_MEMBER} \
  --gke-cluster ${REGION1}-c/cluster-${PROJECT_MEMBER} \
  --project=${PROJECT_MEMBER}
```
```
gcloud container fleet delete --project ${PROJECT_MEMBER}
```





### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/fleet/readme.md)
