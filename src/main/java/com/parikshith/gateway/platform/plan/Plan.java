package com.parikshith.gateway.platform.plan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * A rate-limit tier a {@code Client} can be subscribed to (FREE, PRO, ENTERPRISE, ...).
 * Seeded by {@code V5__seed_default_plans.sql}; managed at runtime once the Developer
 * Platform's plan-management API exists.
 */
@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "requests_per_window", nullable = false)
    private Integer requestsPerWindow;

    @Column(name = "window_seconds", nullable = false)
    private Integer windowSeconds;

    @Column(name = "burst_capacity", nullable = false)
    private Integer burstCapacity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
