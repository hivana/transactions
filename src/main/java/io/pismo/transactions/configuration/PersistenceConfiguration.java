package io.pismo.transactions.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@ComponentScan("io.pismo.transactions.modules")
@EntityScan(basePackages = {
        "io.pismo.transactions.modules.account.persistence.entity",
        "io.pismo.transactions.modules.operationtype.persistence.entity",
        "io.pismo.transactions.modules.transaction.persistence.entity"
})
@EnableJpaRepositories(basePackages = {
        "io.pismo.transactions.modules.account.persistence.repository",
        "io.pismo.transactions.modules.operationtype.persistence.repository",
        "io.pismo.transactions.modules.transaction.persistence.repository"
})
public class PersistenceConfiguration {
}
