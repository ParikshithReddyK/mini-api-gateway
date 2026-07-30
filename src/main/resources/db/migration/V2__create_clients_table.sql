CREATE TABLE clients
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(150) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    plan_id    UUID         NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT uq_clients_email UNIQUE (email),
    CONSTRAINT fk_clients_plan FOREIGN KEY (plan_id) REFERENCES plans (id),
    CONSTRAINT chk_clients_status CHECK (status IN ('ACTIVE', 'DISABLED'))
);

CREATE INDEX idx_clients_plan_id ON clients (plan_id);

COMMENT ON TABLE clients IS 'A registered consumer of the gateway - an application or team, not an individual end user.';
