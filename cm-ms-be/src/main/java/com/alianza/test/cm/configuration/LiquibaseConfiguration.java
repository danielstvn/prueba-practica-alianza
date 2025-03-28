package com.alianza.test.cm.configuration;

import com.alianza.test.cm.configuration.liquibase.SpringLiquibaseUtil;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
public class LiquibaseConfiguration {
    private final Logger logger = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    private final Environment environment;

    public LiquibaseConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SpringLiquibase liquibase(@Qualifier("taskExecutor") Executor executor,
                                     @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource, LiquibaseProperties liquibaseProperties,
                                     ObjectProvider<DataSource> dataSource, DataSourceProperties dataSourceProperties) {

               SpringLiquibase liquibase = SpringLiquibaseUtil.createAsyncSpringLiquibase(this.environment, executor,
                       liquibaseDataSource.getIfAvailable(), liquibaseProperties,
                       dataSource.getIfUnique(), dataSourceProperties);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setLabelFilter(liquibaseProperties.getLabelFilter());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        if (environment.acceptsProfiles(Profiles.of("no-liquibase"))) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            logger.debug("Configuring Liquibase");
        }
        return liquibase;
    }
}
