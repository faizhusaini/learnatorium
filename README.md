# Learnatorium Principal Demo

This directory contains a standalone presentation prototype for Learnatorium Primary School. It is **not** the production ERP, has no backend, uses mock data and browser `localStorage`, and does not import from or modify the production application. The whole directory can be deleted without affecting production.

## Run locally

From the repository root, use either:

```powershell
python -m http.server 5500 --directory principal-demo
```

or any static HTTP server, then open `http://localhost:5500`. Internet access is needed for the Bootstrap, Bootstrap Icons, Chart.js, and Google Fonts CDNs.

Use the **View As** selector to switch instantly between Principal, Teacher, and Parent experiences. Attendance, homework, announcements, child selection, and demo fee payment persist in browser storage. Use **Reset Demo Data** in the user menu to restore the original scenario.

No real authentication, payment, API, database, or personal data is used. All names and records are fictional demonstration data.

