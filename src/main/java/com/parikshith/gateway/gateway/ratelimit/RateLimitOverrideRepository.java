package com.parikshith.gateway.gateway.ratelimit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RateLimitOverrideRepository extends JpaRepository<RateLimitOverride, UUID> {

    Optional<RateLimitOverride> findByClientId(UUID clientId);
}
