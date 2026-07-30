CREATE TABLE plans
(
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code                VARCHAR(50)  NOT NULL,
    name                VARCHAR(100) NOT NULL,
    requests_per_window INTEGER      NOT NULL,
    window_seconds      INTEGER      NOT NULL,
    burst_capacity      INTEGER      NOT NULL,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT uq_plans_code UNIQUE (code),
    CONSTRAINT chk_plans_requests_per_window_positive CHECK (requests_per_window > 0),
    CONSTRAINT chk_plans_window_seconds_positive CHECK (window_seconds > 0),
    CONSTRAINT chk_plans_burst_capacity_positive CHECK (burst_capacity > 0)
);

COMMENT ON TABLE plans IS 'Rate-limit tiers a client can be subscribed to (e.g. FREE, PRO, ENTERPRISE).';
