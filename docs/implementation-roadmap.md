# Learnatorium Implementation Roadmap

## Delivery approach

Work proceeds in vertical slices. A module is complete only when its migration, domain/entity model, repository, application service, DTO/controller, validation, authorization, unit/integration tests, UI states, Playwright journey, and documentation are present. Critical business rules cannot be deferred as TODOs.

## Phase 0 — Foundation documents (current)

- Product scope, architecture, logical schema, security model, and roadmap.
- Decisions recorded for tenancy, IDs, authentication delivery, money, outbox, and offline attendance.
- Proposed repository layout and Phase 1 entry criteria.

Exit: documents reviewed and approval received to scaffold Phase 1.

## Phase 1 — Engineering foundation and identity

- Monorepo structure, Java 21/Spring Boot and Next.js/TypeScript projects.
- Docker Compose with PostgreSQL and MinIO; environment templates and local adapter defaults.
- CI formatting, linting, unit/integration test jobs, architecture tests, and OpenAPI generation.
- Baseline Flyway schema for schools, users, RBAC, refresh tokens, audit, outbox, and idempotency.
- Authentication, refresh rotation, logout/revocation, rate limiting, correlation IDs, problem details, security headers, health endpoints, structured logging.
- Permission-based backend policies and capability endpoint; basic sign-in and admin shell.

Exit: seeded local administrator can sign in; tenant crossing and token-reuse tests pass; no feature data is exposed without policy checks.

## Phase 2 — School setup and people

- School profile, branding, locale/timezone, academic-year lifecycle.
- Students, guardians, multi-child links, staff, status changes, and document metadata.
- CSV/XLSX staged import with validation, preview, error correction, atomic commit, and export.
- Admin lists/details/forms and parent child-switching foundation.

Exit: administrators can configure a year and manage/import people with full tenant/audit coverage.

## Phase 3 — Academics

- Class levels, sections, subjects, enrollments, teacher assignments, periods, and rooms.
- Assignment-aware authorization reusable by downstream modules.
- Admin academic setup and teacher assigned-student views.

Exit: a complete academic structure and active enrollments/assignments exist for a year.

## Phase 4 — Attendance

- Sessions, bulk records, correction workflow, leave hooks, daily/monthly/student/low-attendance reports, absence alerts.
- Mobile teacher marking interface and PWA IndexedDB sync queue with idempotency/conflict handling.
- Parent/student attendance views and admin reports.

Exit: online/offline, duplicate, concurrent, correction, assignment, and IDOR scenarios pass.

## Phase 5 — Timetable and homework

- Periods, class/teacher timetables, rooms, conflicts, substitutions, and notices.
- Homework publishing, safe attachments, optional submissions, and status views.
- Teacher workflows and parent/student timelines.

Exit: conflicts are rejected and publishing produces idempotent notification events.

## Phase 6 — Communication and leave

- Announcements/audiences, calendar, acknowledgement/read tracking, scoped messaging.
- Notification abstraction with in-app and FCM first; email/SMS/WhatsApp adapters behind ports.
- Student and staff leave approval; attendance integration.

Exit: targeted delivery cannot leak recipients across scopes and channel failure is retryable/observable.

## Phase 7 — Fees

- Plans, components/installments, assignments, concessions, invoices, payments, allocations, receipts, refunds, balances, and reports.
- Offline collection and Razorpay adapter with verified, replay-safe webhooks.
- Accountant/admin and parent experiences.

Exit: reconciliation, concurrency, reversal, webhook replay, and audit tests pass; no hard-delete path exists.

## Phase 8 — Exams and report cards

- Exams, assessments, subject rules, grade scales, spreadsheet-style marks entry, locking/publishing, remarks.
- PDF report cards stored as documents; parent/student results.

Exit: assignment boundaries, mark invariants, concurrent edits, publication state, and regenerated document versioning pass.

## Phase 9 — Transport, reporting, privacy, and hardening

- Transport routes/stops/assignments and transport-manager experience.
- Cross-module dashboards, common exports, scheduled reports.
- Privacy request/retention workflows, consent/notice management.
- Localization for Hindi, Telugu, Urdu/RTL; accessibility audit; performance/load tests; backup/restore drill; production runbooks.

Exit: production readiness checklist, security review, operational ownership, and school acceptance testing are complete.

## Proposed repository structure

```text
schoolconnect/
├── apps/
│   ├── api/                    # Spring Boot modular monolith
│   │   └── src/{main,test}/java/.../{module}/
│   └── web/                    # Next.js PWA
│       └── src/{app,components,features,lib,i18n}/
├── packages/
│   ├── api-client/             # generated typed client
│   ├── eslint-config/
│   └── tsconfig/
├── docs/
│   ├── decisions/              # architecture decision records
│   ├── product-spec.md
│   ├── architecture.md
│   ├── database-schema.md
│   ├── security-model.md
│   └── implementation-roadmap.md
├── infra/
│   ├── compose/
│   └── docker/
├── tests/e2e/                  # Playwright cross-experience journeys
├── .github/workflows/
├── compose.yaml
├── Makefile
└── README.md
```

Within each backend module, packages are `api`, `application`, `domain`, and `infrastructure`; shared code is limited to small platform concerns such as IDs, time, problem responses, and security context. Frontend feature folders own their queries, schemas, forms, and views; shared components remain domain-neutral.
