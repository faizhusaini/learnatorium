create extension if not exists pgcrypto;

create table schools (
  id uuid primary key default gen_random_uuid(), code varchar(32) not null unique, name varchar(160) not null,
  timezone varchar(64) not null default 'Asia/Kolkata', default_locale varchar(12) not null default 'en', status varchar(24) not null,
  created_at timestamptz not null default now(), created_by uuid, updated_at timestamptz not null default now(), updated_by uuid
);
create table academic_years (
  id uuid primary key default gen_random_uuid(), school_id uuid not null references schools(id), name varchar(64) not null,
  start_date date not null, end_date date not null, status varchar(24) not null,
  created_at timestamptz not null default now(), created_by uuid, updated_at timestamptz not null default now(), updated_by uuid,
  constraint ck_academic_year_dates check (end_date > start_date), unique(school_id,name), unique(id,school_id)
);
create unique index uq_one_active_academic_year on academic_years(school_id) where status='ACTIVE';

create table users (
  id uuid primary key default gen_random_uuid(), school_id uuid not null references schools(id), email_normalized varchar(254) not null,
  phone_normalized varchar(32), password_hash varchar(255) not null, display_name varchar(160) not null, preferred_locale varchar(12) not null default 'en',
  status varchar(24) not null, failed_login_count integer not null default 0, locked_until timestamptz,
  created_at timestamptz not null default now(), created_by uuid, updated_at timestamptz not null default now(), updated_by uuid,
  unique(school_id,email_normalized), unique(id,school_id)
);
create table roles (
  id uuid primary key default gen_random_uuid(), school_id uuid not null references schools(id), code varchar(64) not null, name varchar(100) not null,
  system_managed boolean not null default false, created_at timestamptz not null default now(), created_by uuid, updated_at timestamptz not null default now(), updated_by uuid,
  unique(school_id,code), unique(id,school_id)
);
create table permissions (id uuid primary key default gen_random_uuid(), code varchar(100) not null unique, description varchar(255) not null);
create table role_permissions (
  school_id uuid not null references schools(id), role_id uuid not null, permission_id uuid not null references permissions(id),
  primary key(role_id,permission_id), foreign key(role_id,school_id) references roles(id,school_id)
);
create index ix_role_permissions_school on role_permissions(school_id,role_id);
create table user_roles (
  id uuid primary key default gen_random_uuid(), school_id uuid not null references schools(id), user_id uuid not null, role_id uuid not null,
  created_at timestamptz not null default now(), created_by uuid,
  foreign key(user_id,school_id) references users(id,school_id), foreign key(role_id,school_id) references roles(id,school_id), unique(school_id,user_id,role_id)
);
create index ix_user_roles_lookup on user_roles(school_id,user_id);

create table refresh_tokens (
  id uuid primary key, school_id uuid not null references schools(id), user_id uuid not null, family_id uuid not null,
  token_hash varchar(64) not null unique, expires_at timestamptz not null, revoked_at timestamptz,
  created_at timestamptz not null default now(), foreign key(user_id,school_id) references users(id,school_id)
);
create index ix_refresh_token_family on refresh_tokens(school_id,user_id,family_id);
create table audit_logs (
  id uuid primary key default gen_random_uuid(), school_id uuid not null references schools(id), actor_id uuid,
  action varchar(100) not null, resource_type varchar(100) not null, resource_id uuid, old_value jsonb, new_value jsonb,
  occurred_at timestamptz not null default now(), correlation_id varchar(64) not null, ip_address inet, user_agent varchar(512)
);
create index ix_audit_resource on audit_logs(school_id,resource_type,resource_id,occurred_at desc);
create or replace function prevent_audit_mutation() returns trigger language plpgsql as $$ begin raise exception 'audit logs are append-only'; end $$;
create trigger audit_no_update_delete before update or delete on audit_logs for each row execute function prevent_audit_mutation();
create table outbox_events (
  id uuid primary key default gen_random_uuid(), school_id uuid not null references schools(id), aggregate_type varchar(100) not null,
  aggregate_id uuid, event_type varchar(160) not null, payload jsonb not null, occurred_at timestamptz not null default now(),
  published_at timestamptz, attempts integer not null default 0, status varchar(24) not null default 'PENDING'
);
create index ix_outbox_pending on outbox_events(status,occurred_at) where status='PENDING';
create table idempotency_keys (
  id uuid primary key default gen_random_uuid(), school_id uuid not null references schools(id), actor_id uuid,
  operation_key varchar(100) not null, request_hash varchar(64) not null, response_status integer, response_body jsonb,
  created_at timestamptz not null default now(), expires_at timestamptz not null, unique(school_id,actor_id,operation_key)
);

insert into permissions(code,description) values
 ('school:manage','Manage school configuration'),('academic_year:manage','Manage academic years'),('user:read','Read users'),
 ('user:manage','Manage users'),('role:assign','Assign roles'),('audit:read','Read audit records'),('student:read','Read authorized students'),
 ('student:manage','Manage students'),('attendance:mark','Mark assigned attendance'),('attendance:correct','Correct attendance'),
 ('marks:enter','Enter assigned marks'),('marks:publish','Publish marks'),('fees:collect','Record fee payments'),('fees:refund','Refund payments'),
 ('announcement:publish','Publish announcements');

