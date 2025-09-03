
## Cloud DNS

```

gcloud container clusters update sample-cluster \
    --cluster-dns=clouddns \
    --cluster-dns-scope=cluster \
    --location=${REGION1}-c

gcloud container clusters update sample-cluster \
    --additive-vpc-scope-dns-domain=sample-cluster-domain \
    --location=${REGION1}-c
```
