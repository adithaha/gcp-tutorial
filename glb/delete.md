# Delete all resources

# Scale down to zero
```
gcloud compute instance-groups managed resize mig-lb-instance-group-${VPC1}-${REGION1} --region=${REGION1} --size=0
```

# Delete all resources
## Regional

```
gcloud compute forwarding-rules delete rlb-fw-rule-${VPC1}-${REGION1} --region=${REGION1}
gcloud compute target-http-proxies delete rlb-proxy-${VPC1}-${REGION1} --region=${REGION1}
gcloud compute url-maps delete rlb-map-http-${VPC1}-${REGION1} --region=${REGION1}
gcloud compute backend-services delete rlb-backend-${VPC1}-${REGION1} --region=${REGION1}
```

## Global
```
gcloud compute forwarding-rules delete glb-fw-rule-${VPC1}-${REGION1} --global
gcloud compute target-http-proxies delete glb-proxy-${VPC1}-${REGION1} --global
gcloud compute url-maps delete glb-map-http-${VPC1}-${REGION1} --global
gcloud compute backend-services delete glb-backend-${VPC1}-${REGION1} --global
```

## MIG
```
gcloud compute instance-groups managed delete mig-lb-instance-group --region asia-southeast2
gcloud compute instance-templates delete mig-lb-template
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/glb/readme.md)
