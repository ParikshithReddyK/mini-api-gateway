package com.parikshith.gateway.platform;

import com.parikshith.gateway.platform.apikey.ApiKey;
import com.parikshith.gateway.platform.apikey.ApiKeyRepository;
import com.parikshith.gateway.platform.apikey.ApiKeyStatus;
import com.parikshith.gateway.platform.client.Client;
import com.parikshith.gateway.platform.client.ClientRepository;
import com.parikshith.gateway.platform.client.ClientStatus;
import com.parikshith.gateway.platform.plan.Plan;
import com.parikshith.gateway.platform.plan.PlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifies the Flyway migrations run cleanly against a real PostgreSQL instance, and that
 * each entity round-trips through its repository against the schema those migrations
 * created.
 *
 * <p>This deliberately runs against real Postgres via Testcontainers rather than H2.
 * H2's Postgres-compatibility mode does not understand {@code gen_random_uuid()} defaults
 * or partial indexes ({@code WHERE status = 'ACTIVE'}) the same way Postgres does - a
 * migration that silently "passes" against H2 is not evidence it works in production.
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class SchemaMigrationIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Test
    void seedMigrationLoadsTheThreeDefaultPlans() {
        List<Plan> plans = planRepository.findAll();

        assertThat(plans).hasSize(3);
        assertThat(plans).extracting(Plan::getCode)
                .containsExactlyInAnyOrder("FREE", "PRO", "ENTERPRISE");
    }

    @Test
    void clientCanBePersistedAgainstASeededPlanAndDefaultsApply() {
        Plan freePlan = planRepository.findByCode("FREE").orElseThrow();

        Client saved = clientRepository.save(Client.builder()
                .name("Acme Inc")
                .email(uniqueEmail("acme"))
                .plan(freePlan)
                .build());

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(ClientStatus.ACTIVE); // @Builder.Default applied
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void duplicateClientEmailIsRejectedByTheUniqueConstraint() {
        Plan freePlan = planRepository.findByCode("FREE").orElseThrow();
        String email = uniqueEmail("dupe");

        clientRepository.saveAndFlush(Client.builder()
                .name("First")
                .email(email)
                .plan(freePlan)
                .build());

        assertThatThrownBy(() -> clientRepository.saveAndFlush(Client.builder()
                .name("Second")
                .email(email)
                .plan(freePlan)
                .build()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void apiKeyLookupByHashFindsTheOwningClient() {
        Plan proPlan = planRepository.findByCode("PRO").orElseThrow();
        Client client = clientRepository.save(Client.builder()
                .name("Beta LLC")
                .email(uniqueEmail("beta"))
                .plan(proPlan)
                .build());

        apiKeyRepository.save(ApiKey.builder()
                .client(client)
                .keyPrefix("mag_live_a1b2")
                .keyHash("dummy-sha256-hash-value")
                .status(ApiKeyStatus.ACTIVE)
                .build());

        Optional<ApiKey> found = apiKeyRepository.findByKeyHash("dummy-sha256-hash-value");

        assertThat(found).isPresent();
        assertThat(found.get().getClient().getId()).isEqualTo(client.getId());
    }

    private static String uniqueEmail(String localPartPrefix) {
        return localPartPrefix + "+" + Instant.now().toEpochMilli() + "@example.com";
    }
}
