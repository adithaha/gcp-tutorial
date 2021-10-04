# Delete all resources

```
gcloud compute forwarding-rules delete mig-lb-content-rule --global
gcloud compute target-http-proxies delete mig-lb-proxy
gcloud compute url-maps delete mig-lb-map-http
gcloud compute backend-services delete mig-lb-backend --global
gcloud compute instance-groups managed delete mig-lb-instance-group --region asia-southeast2
gcloud compute instance-templates delete mig-lb-template
```
