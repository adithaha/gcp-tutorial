## Delete GKE
### Delete Service and Deployment
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

### Unregister from Fleet
```
gcloud container fleet memberships unregister cluster-${PROJECT_MEMBER} \
  --gke-uri "https://container.googleapis.com/v1/projects/${PROJECT_MEMBER}/locations/${REGION1}-c/clusters/cluster-${PROJECT_MEMBER}" \
  --project=${PROJECT_HOST}
gcloud container fleet memberships unregister cluster-${PROJECT_HOST} \
  --gke-cluster ${REGION1}-c/cluster-${PROJECT_HOST} \
  --project=${PROJECT_HOST}
```
### Delete GKE Cluster
```
gcloud container clusters delete cluster-${PROJECT_MEMBER} \
  --zone "${REGION1}-c" \
  --project=${PROJECT_MEMBER} --async
gcloud container clusters delete cluster-${PROJECT_HOST} \
  --zone "${REGION1}-c" \
  --project=${PROJECT_HOST} --async
```

### Delete GKE Fleet
```
gcloud container fleet delete --project ${PROJECT_HOST}
```


### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/gke/fleet/readme.md)
