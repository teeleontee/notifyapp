package edu.java.configuration;

import edu.java.dao.jooq.JooqLinkService;
import edu.java.dao.jooq.JooqLinkUpdater;
import edu.java.dao.jooq.JooqTgChatService;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "connection-type", havingValue = "jooq")
public class JooqConfig {

    private final DataSource dataSource;

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(
            new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public JooqExceptionTranslator exceptionTransformer() {
        return new JooqExceptionTranslator();
    }

    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration
            .set(new DefaultExecuteListenerProvider(exceptionTransformer()));

        return jooqConfiguration;
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    @Bean
    public JooqLinkService jpaLinkService(DefaultDSLContext dsl) {
        return new JooqLinkService(dsl);
    }

    @Bean
    public JooqLinkUpdater jpaLinkUpdater(DefaultDSLContext dsl) {
        return new JooqLinkUpdater(dsl);
    }

    @Bean
    public JooqTgChatService jpaTgChatService(DefaultDSLContext dsl) {
        return new JooqTgChatService(dsl);
    }
}
