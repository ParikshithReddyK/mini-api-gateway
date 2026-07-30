package com.parikshith.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Mini API Gateway + Developer Platform.
 *
 * <p>This is a single deployable Spring Boot application, not a set of microservices.
 * The "Gateway" and "Developer Platform" boxes in the architecture diagram are internal
 * package boundaries (com.parikshith.gateway.gateway.*, com.parikshith.gateway.platform.*),
 * not separate processes. See the README's Architecture Decisions section for why.
 */
@SpringBootApplication
public class MiniApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniApiGatewayApplication.class, args);
    }
}
