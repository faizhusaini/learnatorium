# Learnatorium Database Schema

## 1. Conventions

PostgreSQL tables use `snake_case`, UUID primary keys, and foreign keys with explicit delete behavior. Core business rows include `id`, `school_id`, `created_at`, `created_by`, `updated_at`, and `updated_by`; academic records also include `academic_year_id` where meaningful. Mutable aggregates include `version` for optimistic locking. Timestamps are `timestamptz`; dates remain local calendar dates. Money is `numeric(19,2)` with `currency char(3)`. Statuses are constrained strings mapped from application enums.

Sensitive entities use deactivation/status or retention workflows rather than cascade deletion. Financial and audit records are immutable or reversed with compensating records.

## 2. Identity and tenancy

- `schools`: code, name, timezone, default_locale, brand settings, status.
- `academic_years`: school, name, start/end dates, status; unique `(school_id, name)` and only one active year per school.
- `users`: login identity, normalized email/phone, password hash, status, failed-login fields, preferred locale.
- `roles`, `permissions`, `role_permissions`, `user_roles`: school-scoped role assignment; system role templates may have nullable school only when managed by super-admin logic.
- `refresh_tokens`: user, school context, family ID, token hash, expiry, revoked/replaced fields.
- `consent_records`, `privacy_requests`: notice version, purpose/status, requester, timestamps.

## 3. People and academics

- `students`: admission number, names, date of birth, status, joining/leaving dates; unique `(school_id, admission_number)`.
- `guardians`: linked optional user, names, contacts, status.
- `student_guardians`: student, guardian, relationship, primary/contact/pickup flags; unique pair.
- `staff`: linked user, employee number, staff type, joining/leaving dates, status; unique employee number per school.
- `class_levels`: name, display order.
- `sections`: academic year, class level, name, capacity, optional class teacher; unique within class/year.
- `enrollments`: student, year, section, roll number, status; one active enrollment per student/year.
- `subjects`: code, name, type; unique code per school.
- `teacher_assignments`: staff, year, section, subject, valid dates and role; constrained against duplicate active assignment.

## 4. Timetable and attendance

- `period_definitions`: year, name/number, start/end local time, type (`TEACHING`, `BREAK`).
- `rooms`: code, name, capacity.
- `timetable_slots`: year, section, weekday, period, subject, teacher assignment, room, validity range.
- `substitutions`: slot/date, replacement staff, reason, status.
- `attendance_sessions`: year, section, date, optional period/subject, type, status, marked/submitted metadata, client operation ID. A partial unique index prevents duplicate active sessions for the same section/date/type/period.
- `attendance_records`: session, student, status, remark, source leave; unique `(session_id, student_id)`.
- `attendance_corrections`: record, old/new status, reason, requested/approved/applied metadata.

Conflict detection uses transactional service checks plus exclusion/unique constraints where PostgreSQL can express overlap safely.

## 5. Homework and communication

- `homework`: year, section, subject, teacher, title, description, due timestamp, publish/status fields.
- `homework_attachments`: homework, document, display name.
- `homework_submissions`: homework, student, submitted timestamp, text, document, status; unique student/homework.
- `announcements`: author, title, body, publish/expiry timestamps, priority, acknowledgement requirement, status.
- `announcement_audiences`: announcement plus audience type and target ID.
- `announcement_acknowledgements`: announcement, user, read/acknowledged timestamps; unique pair.
- `calendar_events`: year, title, description, start/end, audience and event type.
- `conversation_threads`, `conversation_participants`, `messages`: scoped parent/teacher communication with policy-controlled participants.

## 6. Fees

- `fee_plans`: year, name, applicability, status.
- `fee_components`: plan, name/type, amount, due date/installment sequence.
- `concessions`: student, component/plan scope, fixed or percentage value, reason, approval/status.
- `student_fee_assignments`: student, plan, effective dates, transport inclusion.
- `invoices`: student, invoice number, issue/due dates, totals, balance, status; unique invoice number per school.
- `invoice_items`: invoice, component, description, quantity, unit amount, discount, tax, total.
- `payments`: student, provider, provider payment/order IDs, amount, method, received timestamp, status, idempotency key.
- `payment_allocations`: payment, invoice, amount.
- `receipts`: payment, receipt number, issued/cancelled metadata; unique number per school.
- `refunds`: payment, amount, provider reference, reason, status.
- `payment_webhook_events`: provider, external event ID, payload hash/encrypted reference, received/processed/status; unique provider event per school.

Database checks enforce non-negative amounts and allocation/refund limits where feasible; services lock relevant rows and enforce aggregate totals.

## 7. Exams and leave

- `grade_scales`, `grade_scale_bands`: name, min/max percentage, grade, points/remark.
- `exams`: year, name, type, start/end dates, status, grade scale.
- `assessments`: exam, section, name, weight, date/status.
- `assessment_subjects`: assessment, subject, maximum/pass marks, assigned teacher.
- `student_marks`: assessment subject, student, marks, absence/exemption flags, remarks, entered/published metadata; unique pair.
- `report_cards`: exam, student, version, totals/result, generated document, published timestamp.
- `leave_requests`: requester/user, subject type and student/staff ID, date range, reason, status, reviewer/comments.

## 8. Platform records

- `notifications`: recipient, type, title/body template data, source event, status.
- `notification_deliveries`: notification, channel, provider, attempts, delivery/read status and timestamps.
- `documents`: owner type/ID, object key, original name, media type, size, checksum, classification, scan/status, retention date.
- `audit_logs`: immutable school, actor, action, resource type/ID, old/new JSON, occurred timestamp, correlation ID, IP/user-agent metadata.
- `outbox_events`: aggregate/source, event type, payload JSON, occurred/published timestamps, attempts/status.
- `idempotency_keys`: school, actor/client, operation key, request hash, response reference, expiry; unique scope/key.

## 9. Indexing and migration policy

Every tenant query path begins with a composite index containing `school_id`; common indexes cover status, academic year, dates, user lookup, enrollment, assignment, and unread/pending work. Indexes follow observed query plans rather than speculative proliferation. Flyway owns all DDL and seed/reference data. Migrations are forward-only, reviewed for locks/backfills, tested from empty and prior release states, and never edited after merge.
