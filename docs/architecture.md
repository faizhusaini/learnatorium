# Learnatorium Architecture

## 1. System shape

Learnatorium is a modular monolith with two deployable applications: a Next.js PWA and a Java 21 Spring Boot API, backed by PostgreSQL and S3-compatible storage. Firebase Cloud Messaging and external email/SMS/WhatsApp/payment providers are accessed only through adapters. Docker Compose supplies local dependencies.

```text
Browser/PWA -> Next.js -> /api/v1 -> Spring Boot modular monolith -> PostgreSQL
                                      |        |-> object storage
                                      |        |-> notification adapters
                                      +----------> payment adapter
```

The backend is packaged by business module. A module owns its entities, repositories, services, API DTOs/controllers, policies, events, and migrations. Other modules may call a documented application service or consume an event; they must not access another module's repositories or tables directly. Architecture tests enforce package dependencies.

## 2. Backend modules

`auth` owns credentials, sessions/tokens, login controls, and authentication events. `users` owns user identity, roles, permissions, and account lifecycle. `schools` owns tenant configuration, branding, locale/timezone, retention, and academic years. `academics` owns class levels, sections, subjects, enrollment, and teacher assignments. Remaining domain modules are `students`, `guardians`, `staff`, `attendance`, `timetable`, `homework`, `communications`, `fees`, `exams`, `leave`, `notifications`, `reports`, `transport`, `documents`, and `audit`.

Cross-module references use stable IDs. Transactional domain events are written to an outbox in the same database transaction; an asynchronous publisher dispatches them with retries. Consumers are idempotent. This is used for notifications, audit enrichment, reports, and external integrations without introducing a message broker in Phase 1.

## 3. Layering

Each module follows:

```text
api (controllers, request/response DTOs)
  -> application (use cases, authorization orchestration, transactions)
     -> domain (entities, value objects, policies, events)
        -> infrastructure (JPA, storage/provider adapters)
```

REST never returns JPA entities. Mapping occurs at the API/application boundary. Controllers are thin; transactions reside in application services. Central exception handling produces RFC 9457 problem details with a correlation ID.

## 4. Multi-tenancy

Tenant identity is derived from authenticated membership, not accepted blindly from request bodies. A request selects an authorized school context (explicit school header/path only when the user belongs to multiple schools). All core tables contain `school_id`; unique constraints include it. Repository operations require school scope, and Hibernate filters/query specifications add defense in depth. PostgreSQL row-level security may be enabled as an additional production guard after connection-context handling is proven.

Object authorization verifies both tenant ownership and relationship/scope: guardian-to-student link, student self-link, or teacher assignment. Background jobs carry an explicit school ID. Cache and object-storage keys are tenant-prefixed. Cross-tenant integration tests are mandatory.

## 5. Authentication and authorization

Spring Security authenticates short-lived access tokens. Rotating refresh tokens are stored as hashes, organized into token families, and revoked on reuse. Passwords use an adaptive hash. Permissions are granular actions such as `attendance:mark` or `fees:collect`; roles are school-scoped collections of permissions. Method-level policy services combine permission checks with domain scope. See `security-model.md`.

## 6. Data and consistency

PostgreSQL is authoritative. UUIDv7 (or application-generated UUID where unavailable) is used for sortable identifiers. `numeric(19,2)` plus ISO currency is used for INR money; Java uses `BigDecimal`. Optimistic version columns protect collaborative edits. Flyway migrations are append-only once committed.

Attendance clients generate an operation ID. The server uses uniqueness constraints and an idempotency record to make retries safe. Payment webhooks store provider event IDs before processing, verify signatures against raw bodies, and apply state transitions atomically. Files are uploaded using short-lived signed URLs and become usable only after server-side metadata, size, and type validation.

## 7. Frontend architecture

The Next.js app uses route groups for admin, teacher, parent, and student experiences, sharing a typed API client, TanStack Query cache, React Hook Form/Zod forms, i18n, design tokens, and shadcn/ui primitives. Route guards improve UX only; APIs remain authoritative. Permission-aware components consume capabilities returned by the server rather than encoding role names.

The service worker caches the application shell and selected safe reads. Offline attendance writes are held in IndexedDB with school, session key, operation ID, payload version, timestamps, and sync state. Sync shows conflicts for explicit resolution and never silently overwrites newer server data. Sensitive cached data is minimized and cleared on logout/account switch.

## 8. Operations

Local Compose runs PostgreSQL, MinIO, API, and web; provider emulators/adapters default to no-op or local capture. Production uses HTTPS, external secret management, managed PostgreSQL/object storage, backups with restore drills, and separate worker capacity if outbox volume grows. Spring Actuator exposes restricted liveness/readiness and metrics endpoints. Logs are JSON and carry request/trace, actor, and school IDs while excluding tokens and unnecessary child data.

## 9. Testing strategy

- Unit tests cover domain rules and policy decisions with JUnit/Mockito.
- Testcontainers integration tests cover PostgreSQL constraints, Flyway, repositories, security filters, outbox, and provider adapters.
- Spring MVC/API tests validate problem responses, validation, and OpenAPI compatibility.
- Playwright covers critical admin, teacher, and parent journeys plus accessibility checks.
- Dedicated tests attempt tenant crossing, IDOR, role escalation, duplicate attendance, webhook replay, and concurrent financial updates.
