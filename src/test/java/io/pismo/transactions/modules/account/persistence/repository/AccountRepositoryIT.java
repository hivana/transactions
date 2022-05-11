package io.pismo.transactions.modules.account.persistence.repository;

import io.pismo.transactions.configuration.MappingConfiguration;
import io.pismo.transactions.configuration.PersistenceConfiguration;
import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.account.persistence.entity.AccountEntity;
import io.pismo.transactions.parameterized.AccountAggregator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = {PersistenceConfiguration.class, MappingConfiguration.class})
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=none"})
class AccountRepositoryIT {

    @Autowired
    private AccountRepository accountRepository;

    @ParameterizedTest
    @DisplayName("Should save an account")
    @CsvSource({
            //documentNumber
            "12345678900",
            "12345678901",
            "12345678902",
            "12345678903"
    })
    void shouldSaveAnAccount(@AggregateWith(AccountAggregator.class) Account account) {
        AccountEntity savedAccount = accountRepository.save(new AccountEntity(null, account.documentNumber()));
        assertEquals(savedAccount.getDocumentNumber(), account.documentNumber());
    }

    @ParameterizedTest
    @DisplayName("Should verify the existence of an account")
    @CsvSource({
            //documentNumber
            "12345678900",
            "12345678901",
            "12345678902",
            "12345678903"
    })
    void shouldVerifyTheExistenceOfAnAccount(@AggregateWith(AccountAggregator.class) Account account) {
        AccountEntity savedAccount = accountRepository.save(new AccountEntity(null, account.documentNumber()));
        assertTrue(accountRepository.existsByDocumentNumber(account.documentNumber()));
    }

    @ParameterizedTest
    @DisplayName("Should not save an account without a document number")
    @CsvSource({
            //documentNumber
            "$NULL"
    })
    void shouldNotSaveAnAccountWithoutADocumentNumber(@AggregateWith(AccountAggregator.class) Account account) {
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(new AccountEntity(null, account.documentNumber())));
    }

}