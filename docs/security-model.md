# Learnatorium Security Model

## 1. Trust boundaries

Browsers, PWA storage, request IDs, object IDs, provider callbacks, and uploaded files are untrusted. The Spring API is the policy enforcement point. PostgreSQL is authoritative; adapters receive only the minimum data needed. UI capability checks are convenience, not security.

## 2. Authentication

- Passwords use Argon2id (or an approved adaptive alternative with documented parameters) and breached/common-password controls.
- Access tokens are short lived and audience/issuer validated. Browser delivery prefers secure, `HttpOnly`, `SameSite` cookies with CSRF protection; if bearer tokens are required, they are kept out of persistent browser storage.
- Refresh tokens are high-entropy, hashed at rest, rotated on every use, grouped into families, and the family is revoked when reuse is detected.
- Login and refresh endpoints are rate-limited by account and network signals. Failures are logged without credential contents.
- Session revocation follows password reset, account disablement, privilege changes, or suspected compromise.
- MFA-ready interfaces are retained for privileged roles.

## 3. Authorization model

Authorization combines:

1. authenticated principal and active account;
2. active membership in the selected school;
3. permission from school-scoped role assignments;
4. resource ownership/relationship or assigned academic scope;
5. record state rules (for example, published or locked marks).

Initial role templates are `SUPER_ADMIN`, `PRINCIPAL`, `SCHOOL_ADMIN`, `ACCOUNTANT`, `TEACHER`, `PARENT`, `STUDENT`, and `TRANSPORT_MANAGER`, but code checks permissions, not role names. Example permissions include `student:read`, `student:manage`, `attendance:mark`, `attendance:correct`, `marks:enter`, `marks:publish`, `fees:collect`, `fees:refund`, `announcement:publish`, and `role:assign`.

Policy methods receive school and resource IDs and query tenant-scoped relationships. A teacher's permission is limited to active assignments. A guardian must have an active `student_guardians` link. A student can read only self-authorized resources and cannot access guardian-only financial or communication operations unless explicitly configured.

## 4. Tenant and IDOR controls

School context comes from verified claims/membership and is applied to every repository query. Client-supplied `school_id` is ignored or rejected. Fetch-by-ID methods use `(school_id, id)` and return a consistent not-found response when inaccessible. Nested IDs are revalidated rather than assumed related. Exports, signed file URLs, caches, jobs, websocket topics, and notification recipients use the same scope.

Automated negative tests cover guessed student IDs, guardian link changes, teacher assignment boundaries, cross-school IDs, mixed-school bulk requests, and stale signed URLs.

## 5. Application and API protections

- Zod improves client feedback; Jakarta validation and domain invariants are authoritative.
- CORS uses explicit origins; security headers include CSP, HSTS, frame restrictions, MIME sniffing protection, and a restrictive referrer policy.
- Rate limits distinguish authentication, upload, export, bulk mutation, and webhook endpoints.
- Pagination and server-side limits prevent unbounded reads/exports.
- Provider webhooks use raw-body cryptographic verification, timestamp/replay checks where supported, idempotent event storage, and explicit state machines.
- Secrets live outside source/config artifacts and are rotated through deployment tooling.

## 6. Files and documents

Uploads use allowlisted media types/extensions, bounded sizes/counts, generated object keys, checksums, malware-scanning status, and private buckets. Download authorization is checked immediately before issuing a short-lived signed URL. Original filenames are display metadata only. HTML/SVG and active content are rejected or safely transformed where not required.

## 7. Privacy and retention

Only necessary child data is collected. Fields have data classifications and documented purposes. Notice/consent versions and access/correction requests are recorded. Retention is configurable by record class, with legal/financial retention overriding deletion. Erasure uses deletion or irreversible anonymization where permitted, while immutable audits retain minimized identifiers. Production data is not copied to lower environments without approved masking.

## 8. Audit and monitoring

Append-only audit events cover attendance corrections, marks, fee/receipt changes, roles, student status, and settings. Events record actor, action, resource, before/after values when appropriate, timestamp, and correlation ID; sensitive values are redacted. Authorization failures, login anomalies, refresh reuse, webhook failures, and bulk exports emit security events. Logs never contain passwords, full tokens, provider secrets, card details, or unnecessary child data.

## 9. Financial controls

Financial rows cannot be hard-deleted. Corrections use cancellation, reversal, credit, or refund records with approval permissions and audit. Provider card details never enter Learnatorium. Receipt numbering is tenant-scoped and collision-resistant. Concurrent allocation/refund paths use locking and invariant checks.

## 10. Security verification

CI runs dependency/secret scanning, static analysis, unit and integration policy tests, migration tests, and targeted Playwright authorization tests. Release readiness includes threat-model review for changed trust boundaries, backup/restore validation, least-privilege review, and an incident response/credential-rotation procedure.
