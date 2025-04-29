package Group2.example.UserCasePoint.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
public class DatabaseConfig {

    /**
     * This inner configuration class will be activated when the 'database.enabled' property is set to 'true'
     * (which is the default). It imports the auto-configurations needed for JPA and transactions.
     */
    @Configuration
    @Profile("!disable-database")
    @ConditionalOnProperty(name = "database.enabled", havingValue = "true", matchIfMissing = true)
    @Import({
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class
    })
    public static class DatabaseAutoConfiguration {
        // No additional beans needed
    }
}
