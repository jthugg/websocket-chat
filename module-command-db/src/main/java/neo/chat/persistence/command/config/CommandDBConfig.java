package neo.chat.persistence.command.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = CommandDBConfig.BASE_PACKAGE,
        transactionManagerRef = CommandDBConfig.TRANSACTION_MANAGER
)
public class CommandDBConfig {

    public static final String BASE_PACKAGE = "neo.chat.persistence.command";
    public static final String TRANSACTION_MANAGER = "commandTM";

    @Bean
    PlatformTransactionManager commandTM(EntityManagerFactory enf) {
        return new JpaTransactionManager(enf);
    }

}
