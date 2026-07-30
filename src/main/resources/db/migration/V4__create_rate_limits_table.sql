CREATE TABLE rate_limits
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id           UUID        NOT NULL,
    requests_per_window INTEGER     NOT NULL,
    window_seconds      INTEGER     NOT NULL,
    burst_capacity      INTEGER     NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT uq_rate_limits_client_id UNIQUE (client_id),
    CONSTRAINT fk_rate_limits_client FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE,
    CONSTRAINT chk_rate_limits_requests_per_window_positive CHECK (requests_per_window > 0),
    CONSTRAINT chk_rate_limits_window_seconds_positive CHECK (window_seconds > 0),
    CONSTRAINT chk_rate_limits_burst_capacity_positive CHECK (burst_capacity > 0)
);

COMMENT ON TABLE rate_limits IS 'Per-client override of their plan''s default rate limit. No row for a client means they simply use their plan''s defaults - this table only stores exceptions.';
