package io.pismo.transactions.configuration;

import io.pismo.transactions.modules.account.dto.mapper.AccountResponseMapper;
import io.pismo.transactions.modules.account.dto.mapper.AccountResponseMapperImpl;
import io.pismo.transactions.modules.account.persistence.mapper.AccountEntityMapper;
import io.pismo.transactions.modules.account.persistence.mapper.AccountEntityMapperImpl;
import io.pismo.transactions.modules.operationtype.persistence.mapper.OperationTypeEntityMapper;
import io.pismo.transactions.modules.operationtype.persistence.mapper.OperationTypeEntityMapperImpl;
import io.pismo.transactions.modules.transaction.dto.mapper.TransactionResponseMapper;
import io.pismo.transactions.modules.transaction.dto.mapper.TransactionResponseMapperImpl;
import io.pismo.transactions.modules.transaction.persistence.mapper.TransactionEntityMapper;
import io.pismo.transactions.modules.transaction.persistence.mapper.TransactionEntityMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfiguration {

    @Bean
    AccountEntityMapper accountEntityMapper() {
        return new AccountEntityMapperImpl();
    }

    @Bean
    OperationTypeEntityMapper OperationTypeEntityMapper() {
        return new OperationTypeEntityMapperImpl();
    }

    @Bean
    TransactionEntityMapper transactionEntityMapper() {
        return new TransactionEntityMapperImpl();
    }

    @Bean
    AccountResponseMapper accountResponseMapper() {
        return new AccountResponseMapperImpl();
    }

    @Bean
    TransactionResponseMapper transactionResponseMapper() {
        return new TransactionResponseMapperImpl();
    }

}