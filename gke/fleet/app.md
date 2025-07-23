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
kubectl get endpoints store-member --context cluster-${PROJECT_MEMBER} -n store-member

```
## Check service
```
kubectl get pods -l app=store-host --namespace store-host --context cluster-${PROJECT_HOST}
kubectl exec -i -t <POD-NAME> --namespace store-host --context cluster-${PROJECT_HOST} -- /bin/bash
```
```
wget store-member
wget store-member-service
```


### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/fleet/readme.md)
