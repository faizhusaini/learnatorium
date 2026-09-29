# Learnatorium

Learnatorium is a multi-school-ready K-12 management platform built as a Spring Boot modular monolith with a Next.js PWA.

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 22+
- Docker with Compose v2

## Local development

Copy `.env.example` to `.env`, replace the development secrets, then run `docker compose up --build`. The web app is available at `http://localhost:3000`, the API at `http://localhost:8080/api/v1`, and Swagger UI at `http://localhost:8080/swagger-ui.html`.

For direct development, run `mvn spring-boot:run` in `apps/api` and `npm install && npm run dev:web` at the repository root. Production bootstrapping does not create a default password; provision the first administrator using a controlled deployment process.

See [the implementation roadmap](docs/implementation-roadmap.md) for delivery phases.

