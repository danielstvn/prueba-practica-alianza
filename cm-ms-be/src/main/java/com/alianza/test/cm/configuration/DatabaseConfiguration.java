package com.alianza.test.cm.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.alianza.test.cm.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {
    private final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);
}
