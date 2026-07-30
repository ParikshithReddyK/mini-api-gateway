package com.parikshith.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test for the application context.
 *
 * <p>The only test at this stage, deliberately. It protects against a misconfigured bean,
 * a missing required property, or a classpath conflict that would otherwise only surface
 * when someone tries to actually start the app. Every feature step from here on adds its
 * own focused unit and integration tests on top of this baseline.
 */
@SpringBootTest
class MiniApiGatewayApplicationTests {

    @Test
    void contextLoads() {
        // Intentionally empty: @SpringBootTest starting the ApplicationContext IS the assertion.
    }
}
