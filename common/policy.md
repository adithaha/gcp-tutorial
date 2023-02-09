# Custom policy

## enable policy
Allow all for org policy below
```
compute.vmCanIpForward
compute.vmExternalIpAccess
```
Disable enforce requireShieldedVm and requireOsLogin
```
gcloud resource-manager org-policies disable-enforce compute.requireShieldedVm --project=${PROJECT}
gcloud resource-manager org-policies disable-enforce compute.requireOsLogin --project=${PROJECT}


gcloud resource-manager org-policies disable-enforce iam.allowedPolicyMemberDomains --project=${PROJECT}



gcloud org-policies describe iam.allowedPolicyMemberDomains --project=${PROJECT}
gcloud services enable orgpolicy.policy.get  --project=${PROJECT}
{"name":"projects/${PROJECT}/policies/iam.allowedPolicyMemberDomains","spec":{"rules": [{"allowAll":true}]}}

gcloud org-policies set-policy iam.json



```


