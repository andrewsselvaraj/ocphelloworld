# Loan Management System

A Spring Boot 3 microservice for submitting and retrieving loan applications.

## Run

Requires Java 17 and Maven:

```text
mvn spring-boot:run
```

The service has no CORS configuration: browsers reach it only through a same-origin proxy (the Angular dev server locally, the frontend's `/api` Route on OpenShift).

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
   oc apply -f openshift/configmap.yaml -f openshift/deployment.yaml -f openshift/service.yaml
   oc rollout status deploy/loan-management-system
   ```

The service has no Route of its own. The frontend project (`ocpfrontendbasic`) exposes it at `/api` on the frontend's host, with rate limiting. To call it directly, port-forward it:

```text
oc port-forward svc/loan-management-system 8080:8080
```

To rebuild after pushing new commits, run `oc start-build loan-management-system --follow`. The Deployment rolls out the new image automatically. To build from your local working copy instead of GitHub, run `oc start-build loan-management-system --from-dir=. --follow`.

Health endpoints: `/actuator/health/liveness` and `/actuator/health/readiness`.
