package io.pismo.transactions.modules.transaction.persistence.repository;

import io.pismo.transactions.configuration.MappingConfiguration;
import io.pismo.transactions.configuration.PersistenceConfiguration;
import io.pismo.transactions.modules.account.persistence.entity.AccountEntity;
import io.pismo.transactions.modules.account.persistence.repository.AccountRepository;
import io.pismo.transactions.modules.operationtype.persistence.entity.OperationTypeEntity;
import io.pismo.transactions.modules.operationtype.persistence.repository.OperationTypeRepository;
import io.pismo.transactions.modules.transaction.dto.Transaction;
import io.pismo.transactions.modules.transaction.persistence.entity.TransactionEntity;
import io.pismo.transactions.parameterized.FakeFactory;
import io.pismo.transactions.parameterized.TransactionAggregator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = {PersistenceConfiguration.class, MappingConfiguration.class})
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=none"})
class TransactionRepositoryIT {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @ParameterizedTest
    @DisplayName("Should save a transaction and the reference of its relationships")
    @CsvSource({
            //amount, eventDate, account, operationType
            "-50.0, 2020-01-01T10:32:07.7199222, 1, 1",
            "-23.5, 2020-01-01T10:48:12.2135875, 1, 1",
            "-18.7, 2020-01-02T19:01:23.1458543, 1, 1",
            "60.0, 2020-01-05T09:34:18.5893223, 1, 4"
    })
    void shouldSaveATransactionAndTheReferenceOfItsRelationships(@AggregateWith(TransactionAggregator.class) Transaction transaction) {
        AccountEntity savedMockAccount = accountRepository.save(new AccountEntity(transaction.account().id(), new Random().nextLong()));
        TransactionEntity transactionToSave = FakeFactory.buildTransactionEntityWithTransaction(transaction);
        transactionToSave.setAccount(savedMockAccount);
        OperationTypeEntity retrievedOperationType = operationTypeRepository.findById(transaction.operationType().id()).get();
        TransactionEntity savedTransaction = transactionRepository.save(transactionToSave);
        assertAll(
                () -> assertEquals(savedMockAccount.getId(), savedTransaction.getAccount().getId()),
                () -> assertEquals(retrievedOperationType.getId(), savedTransaction.getOperationType().getId()),
                () -> assertEquals(transaction.amount(), savedTransaction.getAmount()),
                () -> assertEquals(transaction.eventDate(), savedTransaction.getEventDate())
        );
    }

    @ParameterizedTest
    @DisplayName("Should not save a transaction without the required fields")
    @CsvSource({
            //amount, eventDate, account, operationType
            "$NULL, 2020-01-01T10:32:07.7199222, 1, 1",
            "-18.7, 2020-01-01T10:32:07.7199222, $NULL, 1",
            "-18.7, 2020-01-01T10:32:07.7199222, 1, $NULL"
    })
    void shouldNotSaveATransactionWithoutTheRequiredFields(@AggregateWith(TransactionAggregator.class) Transaction transaction) {
        assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.save(FakeFactory.buildTransactionEntityWithTransaction(transaction)));
    }

    @ParameterizedTest
    @DisplayName("Should find all the transactions saved")
    @CsvSource({
            //amount, eventDate, account, operationType
            "-50.0, 2020-01-01T10:32:07.7199222, 1, 1",
            "-23.5, 2020-01-01T10:48:12.2135875, 1, 1",
            "-18.7, 2020-01-02T19:01:23.1458543, 1, 1",
            "60.0, 2020-01-05T09:34:18.5893223, 1, 4"
    })
    void shouldFindAllTheTransactionsSaved(@AggregateWith(TransactionAggregator.class) Transaction transaction) {
        AccountEntity savedMockAccount = accountRepository.save(new AccountEntity(transaction.account().id(), new Random().nextLong()));
        TransactionEntity transactionToSave = FakeFactory.buildTransactionEntityWithTransaction(transaction);
        transactionToSave.setAccount(savedMockAccount);
        TransactionEntity savedTransaction = transactionRepository.save(transactionToSave);
        assertTrue(List.of(savedTransaction).equals(transactionRepository.findAll()));
    }

    @ParameterizedTest
    @DisplayName("Should find a transaction by its id")
    @CsvSource({
            //amount, eventDate, account, operationType
            "-50.0, 2020-01-01T10:32:07.7199222, 1, 1",
            "-23.5, 2020-01-01T10:48:12.2135875, 1, 1",
            "-18.7, 2020-01-02T19:01:23.1458543, 1, 1",
            "60.0, 2020-01-05T09:34:18.5893223, 1, 4"
    })
    void shouldFindATransactionByItsId(@AggregateWith(TransactionAggregator.class) Transaction transaction) {
        AccountEntity savedMockAccount = accountRepository.save(new AccountEntity(transaction.account().id(), new Random().nextLong()));
        TransactionEntity transactionToSave = FakeFactory.buildTransactionEntityWithTransaction(transaction);
        transactionToSave.setAccount(savedMockAccount);
        TransactionEntity savedTransaction = transactionRepository.save(transactionToSave);
        TransactionEntity foundTransaction = transactionRepository.findById(savedTransaction.getId()).get();
        assertAll(
                () -> assertEquals(savedTransaction.getAccount().getId(), foundTransaction.getAccount().getId()),
                () -> assertEquals(savedTransaction.getOperationType().getId(), foundTransaction.getOperationType().getId()),
                () -> assertEquals(savedTransaction.getAmount(), foundTransaction.getAmount()),
                () -> assertEquals(savedTransaction.getEventDate(), foundTransaction.getEventDate())
        );

    }

    @ParameterizedTest
    @DisplayName("Should delete a transaction by its id")
    @CsvSource({
            //amount, eventDate, account, operationType
            "-50.0, 2020-01-01T10:32:07.7199222, 1, 1",
            "-23.5, 2020-01-01T10:48:12.2135875, 1, 1",
            "-18.7, 2020-01-02T19:01:23.1458543, 1, 1",
            "60.0, 2020-01-05T09:34:18.5893223, 1, 4"
    })
    void shouldDeleteATransactionByItsId(@AggregateWith(TransactionAggregator.class) Transaction transaction) {
        AccountEntity savedMockAccount = accountRepository.save(new AccountEntity(transaction.account().id(), new Random().nextLong()));
        TransactionEntity transactionToSave = FakeFactory.buildTransactionEntityWithTransaction(transaction);
        transactionToSave.setAccount(savedMockAccount);
        TransactionEntity savedTransaction = transactionRepository.save(FakeFactory.buildTransactionEntityWithTransaction(transaction));
        transactionRepository.deleteById(savedTransaction.getId());
        assertEquals(Optional.empty(), transactionRepository.findById(savedTransaction.getId()));
    }

}