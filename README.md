# Mini API Gateway + Developer Platform

A lightweight, production-inspired API Gateway in front of backend services, paired with a
developer platform for managing clients, API keys, quotas, and usage.

Not an attempt to compete with Kong, Spring Cloud Gateway, or AWS API Gateway. A deliberately
small, well-engineered implementation demonstrating production engineering practices —
authentication, rate limiting, observability, clean layering, testing, CI/CD — without the
operational overhead of Kubernetes or a real microservices topology.

## Status

Under active development. See the roadmap below for what's implemented.

## Architecture Decisions

- **Modular monolith, not microservices.** Gateway and Developer Platform are separate Java
  packages with clear boundaries, deployed as one Spring Boot application.
- **Maven, single module.** A single `pom.xml` is readable by any reviewer without requiring
  Gradle familiarity.
- **Spring Boot 4.1 / Spring Framework 7 / Java 21.** Current, actively OSS-supported line.
- **Dependencies added incrementally** — one Maven starter per feature step — so the git
  history documents how the system grew.

## Tech Stack

Java 21, Spring Boot 4.1, Spring Security, Redis, PostgreSQL, Flyway, Docker, GitHub Actions,
JUnit 5, Mockito, springdoc-openapi (Swagger).

## Running Locally

```bash
mvn spring-boot:run
```

Starts on `http://localhost:8080` with the `local` profile active.

```bash
curl http://localhost:8080/actuator/health
```

## Roadmap

- [x] Project scaffolding, CI skeleton
- [ ] Database schema (clients, api_keys, plans, rate_limits)
- [ ] JWT authentication
- [ ] API key authentication
- [ ] Redis-based rate limiting
- [ ] Request routing / proxying
- [ ] Standardized response & error handling
- [ ] Observability (correlation ID, metrics, structured logging)
- [ ] Client management API
- [ ] API key management API
- [ ] Usage analytics
- [ ] Swagger / OpenAPI documentation
- [ ] Docker & Docker Compose
- [ ] GitHub Actions CI/CD pipeline
- [ ] AWS deployment
- [ ] Open-source polish (CONTRIBUTING, issue templates)

## License

MIT — see [LICENSE](LICENSE).
