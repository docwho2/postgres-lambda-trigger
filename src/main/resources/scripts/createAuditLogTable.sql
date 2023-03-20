

-- Simple table to log audits
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    created timestamp without time zone NOT NULL DEFAULT timezone('utc'::text, now()),
    operation text NOT NULL,
    table_name text NOT NULL,
    old_record jsonb,
    new_record jsonb
);

CREATE TABLE IF NOT EXISTS audit_log_sqs (
    id BIGSERIAL PRIMARY KEY,
    created timestamp without time zone NOT NULL DEFAULT timezone('utc'::text, now()),
    operation text NOT NULL,
    table_name text NOT NULL,
    old_record jsonb,
    new_record jsonb
);