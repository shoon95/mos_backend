package com.mos.backend.testconfig;

import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractTestContainer {

    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");

        // HikariCP 설정: 테스트 환경에서 커넥션 풀의 안정성을 위해 짧은 값 사용
        registry.add("spring.datasource.hikari.maxLifetime", () -> "30000");         // 30초
        registry.add("spring.datasource.hikari.connectionTimeout", () -> "30000");     // 30초
        registry.add("spring.datasource.hikari.idleTimeout", () -> "10000");           // 10초
        registry.add("spring.datasource.hikari.connectionTestQuery", () -> "SELECT 1");

    }
}