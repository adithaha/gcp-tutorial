curl -X POST \
    -H "Authorization: Bearer $(gcloud auth print-access-token)" \
    -H "x-goog-user-project: PROJECT_NUMBER" \
    -H "Content-Type: application/json; charset=utf-8" \
    -d @request.json \
    "https://videointelligence.googleapis.com/v1/videos:annotate"
