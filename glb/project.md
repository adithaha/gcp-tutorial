
# Create Project
Administrator will create project, assign billing account and add non-admin user as project owner.
Login with admin role.
## create project with billing account
```
gcloud projects create --name=gke-test
```
```
PROJECT=[PROJECT_ID]
BILLING_ACCOUNT=[BILLING_ACCOUNT]
```
```
gcloud beta billing projects link ${PROJECT} --billing-account ${BILLING_ACCOUNT}
```
```
gcloud projects add-iam-policy-binding ${PROJECT} --member=user:[USER_EMAIL] --role=roles/owner
```
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
{"name":"projects/gke-test-325904/policies/iam.allowedPolicyMemberDomains","spec":{"rules": [{"allowAll":true}]}}

gcloud org-policies set-policy iam.json
```

### Go back
[Content](https://github.com/adithaha/gcp-tutorial/blob/main/glb/readme.md)
