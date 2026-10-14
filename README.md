# Loan Management System

A Spring Boot 3 microservice for submitting and retrieving loan applications.

## Run

Requires Java 17 and Maven:

```text
mvn spring-boot:run
```

## API

- `POST /api/loan-applications` — submit an application with `applicantName`, `loanType`, and a positive `requestedAmount`.
- `GET /api/loan-applications` — list submitted applications.
- `GET /api/loan-applications/{id}` — retrieve one application; returns `404` if it does not exist.

Applications are held in memory and are cleared when the service restarts.
