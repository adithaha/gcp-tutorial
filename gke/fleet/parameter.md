# Set Parameter
Login with non-admin role.

## Set parameter for GKE FLEET
Project ID
```
PROJECT=[PROJECT_ID]
PROJECT_HOST=[PROJECT_HOST]
PROJECT_MEMBER=[PROJECT_MEMBER]

PROJECT_HOST_NUM=$(gcloud projects describe "${PROJECT_HOST}" --format "value(projectNumber)")

