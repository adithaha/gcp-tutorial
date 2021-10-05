# Adding WAF with Cloud Armor

# Create CA policy
```
gcloud compute security-policies create mig-lb-ca
```

# Create CA rules
```
gcloud compute security-policies rules update 2147483647 \
    --security-policy mig-lb-ca \
    --action "deny-404"

gcloud compute security-policies rules create 1000 \
    --security-policy mig-lb-ca    \
    --expression "origin.region_code == 'ID'" \
    --action allow

gcloud compute security-policies rules create 100 \
    --security-policy mig-lb-ca    \
    --expression "evaluatePreconfiguredExpr('sqli-stable')	" \
    --action allow

gcloud compute security-policies rules create 101 \
    --security-policy mig-lb-ca    \
    --expression "evaluatePreconfiguredExpr('xss-canary', ['owasp-crs-v020901-id981136-xss'])	" \
    --action allow

gcloud compute security-policies rules create 999 \
    --security-policy mig-lb-ca    \
    --expression "request.path.matches('/asd')" \
    --action allow
```

# Map CA to backend
```
gcloud compute backend-services update mig-lb-backend --security-policy mig-lb-ca
```
