package io.pismo.transactions.modules.transaction.service;

import io.pismo.transactions.configuration.exception.ValidationException;
import io.pismo.transactions.modules.account.service.AccountService;
import io.pismo.transactions.modules.operationtype.service.OperationTypeService;
import io.pismo.transactions.modules.transaction.dto.Transaction;
import io.pismo.transactions.modules.transaction.persistence.entity.TransactionEntity;
import io.pismo.transactions.modules.transaction.persistence.mapper.TransactionEntityMapperImpl;
import io.pismo.transactions.modules.transaction.persistence.repository.TransactionRepository;
import io.pismo.transactions.parameterized.FakeFactory;
import io.pismo.transactions.parameterized.TransactionAggregator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class TransactionServiceUT {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private OperationTypeService operationTypeService;

    private AutoCloseable closeable;
    private TransactionService transactionService;
    @Captor
    private ArgumentCaptor<TransactionEntity> transactionCaptor;

    @BeforeEach
    public void beforeEach() {
        this.closeable = MockitoAnnotations.openMocks(this);
        this.transactionService = new TransactionService(transactionRepository, new TransactionEntityMapperImpl(), accountService, operationTypeService);
    }

    @AfterEach
    void afterEach() throws Exception {
        this.closeable.close();
    }

    @Nested
    @DisplayName("Happy path")
    class HappyPath {

        @ParameterizedTest
        @DisplayName("Should save a transaction with valid input")
        @CsvSource({
                //amount, eventDate, account, operationType
                "-50.0, 2020-01-01T10:32:07.7199222, 1, 1",
                "-23.5, 2020-01-01T10:48:12.2135875, 1, 1",
                "-18.7, 2020-01-02T19:01:23.1458543, 1, 1",
                "60.0, 2020-01-05T09:34:18.5893223, 1, 4"
        })
        void shouldSaveATransactionWithValidInput(@AggregateWith(TransactionAggregator.class) Transaction transaction) {

            TransactionEntity mockForSavedTransaction = FakeFactory.buildTransactionEntityWithTransaction(transaction);

            lenient().when(accountService.existsById(transaction.account().id())).thenReturn(true);
            lenient().when(operationTypeService.existsById(transaction.operationType().id())).thenReturn(true);
            lenient().when(transactionRepository.save(ArgumentMatchers.any(TransactionEntity.class))).thenReturn(mockForSavedTransaction);

            Transaction savedTransaction = transactionService.createTransaction(transaction);
            verify(transactionRepository).save(transactionCaptor.capture());
            TransactionEntity repositorySavedTransaction = transactionCaptor.getValue();

            verify(transactionRepository, times(1)).save(ArgumentMatchers.any(TransactionEntity.class));
            assertEquals(savedTransaction.amount(), repositorySavedTransaction.getAmount());

        }

        @ParameterizedTest
        @DisplayName("Should save an transaction with a negative amount if operation type is different than payment")
        @CsvSource({
                //amount, eventDate, account, operationType
                "50.0, 2020-01-01T10:32:07.7199222, 1, 1",
                "23.5, 2020-01-01T10:48:12.2135875, 1, 2",
                "18.7, 2020-01-02T19:01:23.1458543, 1, 3"
        })
        void shouldSaveAnAccountWithANegativeAmountIfOperationTypeIsDifferentThanPayment(@AggregateWith(TransactionAggregator.class) Transaction transaction) {

            lenient().when(accountService.existsById(transaction.account().id())).thenReturn(true);
            lenient().when(operationTypeService.existsById(transaction.operationType().id())).thenReturn(true);
            lenient().when(transactionRepository.save(ArgumentMatchers.any(TransactionEntity.class))).thenReturn(new TransactionEntity());

            transactionService.createTransaction(transaction);
            verify(transactionRepository).save(transactionCaptor.capture());
            TransactionEntity repositorySavedTransaction = transactionCaptor.getValue();

            verify(transactionRepository, times(1)).save(ArgumentMatchers.any(TransactionEntity.class));
            assertTrue(repositorySavedTransaction.getAmount().compareTo(BigDecimal.ZERO) < 0);

        }

        @ParameterizedTest
        @DisplayName("Should save a transaction with a positive amount if operation type is payment")
        @CsvSource({
                //amount, eventDate, account, operationType
                "-50.0, 2020-01-01T10:32:07.7199222, 1, 4",
                "-23.5, 2020-01-01T10:48:12.2135875, 1, 4",
                "-18.7, 2020-01-02T19:01:23.1458543, 1, 4"
        })
        void shouldSaveATransactionWithAPositiveAmountIfOperationTypeIsPaymentThanPayment(@AggregateWith(TransactionAggregator.class) Transaction transaction) {

            lenient().when(accountService.existsById(transaction.account().id())).thenReturn(true);
            lenient().when(operationTypeService.existsById(transaction.operationType().id())).thenReturn(true);
            lenient().when(transactionRepository.save(ArgumentMatchers.any(TransactionEntity.class))).thenReturn(new TransactionEntity());

            transactionService.createTransaction(transaction);
            verify(transactionRepository).save(transactionCaptor.capture());
            TransactionEntity repositorySavedTransaction = transactionCaptor.getValue();

            verify(transactionRepository, times(1)).save(ArgumentMatchers.any(TransactionEntity.class));
            assertTrue(repositorySavedTransaction.getAmount().compareTo(BigDecimal.ZERO) > 0);

        }
    }

    @Nested
    @DisplayName("Not so happy path")
    class NotSoHappyPath {

        @ParameterizedTest
        @DisplayName("Should not save a transaction without an amount")
        @CsvSource({
                //amount, eventDate, account, operationType
                "0, 2020-01-01T10:32:07.7199222, 1, 1",
                "0, 2020-01-01T10:48:12.2135875, 1, 1",
                "$NULL, 2020-01-02T19:01:23.1458543, 1, 1",
                "$NULL, 2020-01-05T09:34:18.5893223, 1, 4"
        })
        void shouldNotSaveATransactionWithoutAnAmount(@AggregateWith(TransactionAggregator.class) Transaction transaction) {

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> transactionService.createTransaction(transaction),
                    "ValidationException expected to be thrown"
            );
            Assertions.assertEquals("Amount must be provided.", exception.getMessage());

            verify(transactionRepository, times(0)).save(ArgumentMatchers.any(TransactionEntity.class));
        }

        @ParameterizedTest
        @DisplayName("Should not save a transaction with an invalid account")
        @CsvSource({
                //amount, eventDate, account, operationType
                "-20.21, 2020-01-01T10:32:07.7199222, 1, 1",
                "-910.2, 2020-01-01T10:48:12.2135875, 1, 2",
                "-93.1, 2020-01-02T19:01:23.1458543, 1, 3",
                "54.2, 2020-01-05T09:34:18.5893223, 1, 4"
        })
        void shouldNotSaveATransactionWithAnInvalidAccount(@AggregateWith(TransactionAggregator.class) Transaction transaction) {

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> transactionService.createTransaction(transaction),
                    "ValidationException expected to be thrown"
            );
            Assertions.assertEquals("Account not found.", exception.getMessage());

            verify(transactionRepository, times(0)).save(ArgumentMatchers.any(TransactionEntity.class));
        }

        @ParameterizedTest
        @DisplayName("Should not save a transaction with an invalid operation type")
        @CsvSource({
                //amount, eventDate, account, operationType
                "-20.21, 2020-01-01T10:32:07.7199222, 1, -1",
                "910.2, 2020-01-01T10:48:12.2135875, 1, 0",
                "-93.1, 2020-01-02T19:01:23.1458543, 1, 5",
                "54.2, 2020-01-05T09:34:18.5893223, 1, 99"
        })
        void shouldNotSaveATransactionWithAnInvalidOperationType(@AggregateWith(TransactionAggregator.class) Transaction transaction) {

            lenient().when(accountService.existsById(transaction.account().id())).thenReturn(true);

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> transactionService.createTransaction(transaction),
                    "ValidationException expected to be thrown"
            );
            Assertions.assertEquals("Operation type not found.", exception.getMessage());

            verify(transactionRepository, times(0)).save(ArgumentMatchers.any(TransactionEntity.class));
        }
    }

}