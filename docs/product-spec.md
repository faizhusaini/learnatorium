# Learnatorium Product Specification

## 1. Purpose

Learnatorium is a secure, accessible school-management platform for an Indian K-12 school in Hyderabad serving approximately 400–500 students. The first deployment serves one school, while every core workflow and data model is tenant-aware so additional schools can be onboarded without re-architecture.

The product has three tailored experiences: desktop-first administration, mobile-first teachers, and mobile-first parents/students. English is the initial locale; Hindi, Telugu, and Urdu (including RTL layout) are planned. Dates and schedules use `Asia/Kolkata` by default and are stored as UTC instants where appropriate.

## 2. Product principles

- Child safety and privacy by design; collect only data needed for school operations.
- Authorization is enforced by backend policy, never by hidden UI controls.
- Fast daily workflows: class attendance, marks entry, fee collection, and communication.
- Financial, assessment, and attendance changes are traceable.
- Mobile workflows tolerate weak or intermittent connectivity.
- Modules integrate through explicit contracts and application events.
- Accessibility target: WCAG 2.2 AA.

## 3. Users and outcomes

### Administration and management

Principals, school administrators, accountants, and transport managers configure the school and academic year; manage people, academics, attendance, timetables, fees, exams, communications, reports, and settings. The dashboard summarizes student and teacher attendance, collections, outstanding fees, completed classes, approvals, and alerts. Quick actions cover student admission, announcements, fee collection, attendance, and reports.

### Teachers

Teachers see today's timetable, next class, attendance work, pending marks/homework, announcements, and substitutions. They can act only on assigned classes, sections, subjects, and students unless an explicit permission broadens scope. Core workflows include bulk attendance, homework/attachments, marks/remarks, parent communication, timetable, and leave.

### Parents and students

The parent home answers “What do I need to know about my child today?” with attendance, homework, timetable, exams, fees, and announcements. One guardian account can switch among linked children. Students are optional and receive a narrower view than guardians. Mobile navigation is Home, Academics, Inbox, and More.

## 4. Functional scope

The modular product areas are: authentication, users, schools, academics, students, guardians, staff, attendance, timetable, homework, communications, fees, exams, leave, notifications, reports, transport, documents, and audit.

Key acceptance constraints:

- Attendance supports `PRESENT`, `ABSENT`, `LATE`, `HALF_DAY`, and `EXCUSED`; one session per class/section/date/period; offline retries are idempotent; corrections require authorization and audit.
- Timetables represent periods, rooms, breaks, teacher assignments, and substitutions, and reject teacher/room/class conflicts.
- Homework targets class, section, and subject, supports files and optional student submissions, and emits notification events.
- Announcements target school, class, section, role groups, or individuals and track sent, delivery, read, and acknowledgement states.
- Fees cover plans, components, installments, concessions, transport fees, invoices, payments, receipts, refunds, balances, and defaulters. Money uses decimal minor-unit-safe types. Financial records are reversed/cancelled, never hard-deleted.
- Razorpay is the first payment adapter. Signatures are verified, webhook events are idempotent, and card data is never stored.
- Exams support types, assessments, subjects, maxima/pass marks, grades, bulk marks entry, remarks, and generated PDF report cards.
- Student and staff leave has approval workflow; approved student leave informs attendance without silently rewriting historical records.
- CSV/XLSX imports use upload, validation, preview/correction, and atomic commit stages.

## 5. Non-functional requirements

- Versioned REST API under `/api/v1` with OpenAPI documentation.
- Strong tenant and object-level isolation, secure authentication, rate limits, input/file validation, and secure headers.
- PWA shell with resilient reads and specifically designed offline attendance writes.
- Structured logs, correlation IDs, health checks, metrics-ready configuration, and redaction of secrets/personal data.
- Loading, empty, success, and actionable error states for important screens.
- Configurable brand color; clean, professional visual system with labeled icons.
- Data retention and consent/notice version tracking designed to support India's DPDP obligations.

## 6. Out of initial scope

The first phases do not include payroll, library, hostel, inventory, learning-video delivery, biometric hardware integration, or a native mobile application. These may be introduced through new modules/adapters without weakening existing boundaries.

## 7. Success measures

- A teacher can mark a typical class in under two minutes, including an offline retry.
- No cross-school or unrelated-child access succeeds in automated security tests.
- Fee totals reconcile from immutable invoice/payment/refund records.
- Common parent information is reachable within two taps after child selection.
- Critical actions have attributable audit records and correlation IDs.
