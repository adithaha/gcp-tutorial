
# Create Project
Administrator will create project, assign billing account and add non-admin user as project owner.
Login with admin role.
## create project with billing account
```
gcloud projects create --name=gcp-tutorial
```
```
PROJECT=[PROJECT_ID]
BILLING_ACCOUNT=[BILLING_ACCOUNT]
gcloud config set project ${PROJECT}
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
sed -i 's/$PROJECT/'"${PROJECT}"'/g' iam/allowedPolicyMemberDomains.json
gcloud org-policies set-policy iam/allowedPolicyMemberDomains.json
```

## set parameter
[Set Parameter](https://github.com/adithaha/gcp-tutorial/blob/main/common/parameter.md)

