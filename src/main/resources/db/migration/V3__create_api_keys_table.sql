CREATE TABLE api_keys
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id    UUID         NOT NULL,
    key_prefix   VARCHAR(16)  NOT NULL,
    key_hash     VARCHAR(255) NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    scopes       VARCHAR(255),
    expires_at   TIMESTAMPTZ,
    last_used_at TIMESTAMPTZ,
    revoked_at   TIMESTAMPTZ,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT fk_api_keys_client FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE,
    CONSTRAINT uq_api_keys_key_hash UNIQUE (key_hash),
    CONSTRAINT chk_api_keys_status CHECK (status IN ('ACTIVE', 'REVOKED'))
);

CREATE INDEX idx_api_keys_client_id ON api_keys (client_id);

-- Partial index: every authenticated request looks up an ACTIVE key by hash. Revoked/expired
-- keys don't need to be in this index at all, which keeps it smaller and faster than a full one.
CREATE INDEX idx_api_keys_status_active ON api_keys (status) WHERE status = 'ACTIVE';

COMMENT ON TABLE api_keys IS 'Hashed API keys. The plaintext secret is shown to the client exactly once at creation time and never stored.';
COMMENT ON COLUMN api_keys.key_hash IS 'SHA-256 hash of the secret portion of the key. Never store the plaintext key.';
COMMENT ON COLUMN api_keys.key_prefix IS 'First few characters of the plaintext key, safe to display in UIs/logs for identification (e.g. mag_live_a1b2).';
