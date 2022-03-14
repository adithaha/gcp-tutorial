# camel-redis

## enable registry
```
gcloud services enable containerregistry.googleapis.com
```

## Build source
```
mvn clean package
```

## Build image
```
docker build -t asia.gcr.io/${PROJECT}/camel-log .
```

## Run image locally
```
docker run asia.gcr.io/gke-test-325904/camel-log
```

## Push image
```
docker push asia.gcr.io/${PROJECT}/camel-log
```

## Deploy
```
kubectl apply -f k8s/deployment.yaml
kubectl describe deployment camel-log-deployment
kubectl get pods -l app=camel-log
POD_NAME=[POD_NAME]
```

Get logs and enter shell
```
kubectl logs -f ${POD_NAME}
kubectl exec -i -t ${POD_NAME} -- /bin/bash
```
