gcloud redis instances create --project=gke-test-325904  redis-1 --tier=basic --size=1 --region=asia-southeast2 --redis-version=redis_6_x --network=projects/gke-test-325904/global/networks/devnet1 --connect-mode=PRIVATE_SERVICE_ACCESS --reserved-ip-range=devnet1-ip-range

mvn exec:java -Dexec.mainClass="com.google.nugraha.memorystore.client.Main"
