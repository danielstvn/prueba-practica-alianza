package com.alianza.test.cm.configuration.liquibase;

import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;

public class AsyncSpringLiquibase extends DataSourceClosingSpringLiquibase {

    public static final String DISABLED_MESSAGE = "Liquibase is disabled";
    public static final String STARTING_ASYNC_MESSAGE = "Starting Liquibase asynchronously, your database might not be ready at startup!";
    public static final String STARTING_SYNC_MESSAGE = "Starting Liquibase synchronously";
    public static final String STARTED_MESSAGE = "Liquibase has updated your database in {} ms";
    public static final String EXCEPTION_MESSAGE = "Liquibase could not start correctly, your database is NOT ready: " + "{}";
    public static final long SLOWNESS_THRESHOLD = 5;
    public static final String SLOWNESS_MESSAGE = "Warning, Liquibase took more than {} seconds to start up!";

    private final Logger logger = LoggerFactory.getLogger(AsyncSpringLiquibase.class);

    private final Executor executor;

    private final Environment env;

    /**
     * Constructor for AsyncSpringLiquibase
     *
     * @param executor a {@link Executor} object.
     * @param env a {@link Environment} object.
     */
    public AsyncSpringLiquibase(Executor executor, Environment env) {
        this.executor = executor;
        this.env = env;
    }

    /** {@inheritDoc} */
    @Override
    public void afterPropertiesSet() throws LiquibaseException {
        if (!env.acceptsProfiles(Profiles.of("no-liquibase"))) {
            if (env.acceptsProfiles(Profiles.of("dev"))) {
                try (Connection connection = getDataSource().getConnection()) {
                    executor.execute(() -> {
                        try {
                            logger.warn(STARTING_ASYNC_MESSAGE);
                            initDb();
                        } catch (LiquibaseException e) {
                            logger.error(EXCEPTION_MESSAGE, e.getMessage(), e);
                        }
                    });
                } catch (SQLException e) {
                    logger.error(EXCEPTION_MESSAGE, e.getMessage(), e);
                }
            } else {
                logger.debug(STARTING_SYNC_MESSAGE);
                initDb();
            }
        } else {
            logger.debug(DISABLED_MESSAGE);
        }
    }

    /**
     * initDb
     *
     * @throws LiquibaseException if any.
     */
    protected void initDb() throws LiquibaseException {
        StopWatch watch = new StopWatch();
        watch.start();
        super.afterPropertiesSet();
        watch.stop();
        logger.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        if (watch.getTotalTimeMillis() > SLOWNESS_THRESHOLD * 1000L) {
            logger.warn(SLOWNESS_MESSAGE, SLOWNESS_THRESHOLD);
        }
    }
}