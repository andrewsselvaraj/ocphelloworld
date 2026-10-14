# Loan Management System

A Spring Boot 3 microservice for submitting and retrieving loan applications.

## Run

Requires Java 17 and Maven:

```text
mvn spring-boot:run
```

## API

- `GET /` — confirm that the service is running and find the loan application API.
- `POST /api/loan-applications` — submit an application with `applicantName`, `loanType`, and a positive `requestedAmount`.
- `GET /api/loan-applications` — list submitted applications.
- `GET /api/loan-applications/{id}` — retrieve one application; returns `404` if it does not exist.

Applications are held in memory and are cleared when the service restarts.

## Deploy to OpenShift (Developer Sandbox)

The image is built inside the cluster from this GitHub repo with the multi-stage `Dockerfile`, so you need no local Maven or Docker. Push your changes to `main` first.

1. Log in: in the Sandbox web console, open your user menu → **Copy login command**, then run the `oc login --token=... --server=...` command it shows.
2. Create the build, then deploy:

   ```text
   oc apply -f openshift/build.yaml
   oc logs -f bc/loan-management-system
   oc apply -f openshift/deployment.yaml -f openshift/service.yaml -f openshift/route.yaml
   oc rollout status deploy/loan-management-system
   ```

3. Get the public URL:

   ```text
   oc get route loan-management-system -o jsonpath='https://{.spec.host}{"\n"}'
   ```

To rebuild after pushing new commits, run `oc start-build loan-management-system --follow`. The Deployment rolls out the new image automatically. To build from your local working copy instead of GitHub, run `oc start-build loan-management-system --from-dir=. --follow`.

Health endpoints: `/actuator/health/liveness` and `/actuator/health/readiness`.
