package io.pismo.transactions.modules.account.service;

import io.pismo.transactions.configuration.exception.ValidationException;
import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.account.persistence.entity.AccountEntity;
import io.pismo.transactions.modules.account.persistence.mapper.AccountEntityMapper;
import io.pismo.transactions.modules.account.persistence.mapper.AccountEntityMapperImpl;
import io.pismo.transactions.modules.account.persistence.repository.AccountRepository;
import io.pismo.transactions.parameterized.AccountAggregator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AccountServiceUT {

    @Mock
    private AccountRepository accountRepository;
    private AutoCloseable closeable;
    private AccountService accountService;
    private AccountEntityMapper accountEntityMapper;

    @BeforeEach
    public void beforeEach() {
        this.closeable = MockitoAnnotations.openMocks(this);
        this.accountEntityMapper = new AccountEntityMapperImpl();
        this.accountService = new AccountService(accountRepository, accountEntityMapper);
    }

    @AfterEach
    void afterEach() throws Exception {
        this.closeable.close();
    }

    @Nested
    @DisplayName("Happy path")
    class HappyPath {

        @ParameterizedTest
        @DisplayName("Should save an account with a valid document number")
        @CsvSource({
                //documentNumber
                "12345678900",
                "12345678901",
                "12345678902",
                "12345678903"
        })
        void shouldSaveAnAccountWithAValidDocumentNumber(@AggregateWith(AccountAggregator.class) Account account) {
            lenient().when(accountRepository.save(ArgumentMatchers.any(AccountEntity.class))).thenReturn(new AccountEntity(null, account.documentNumber()));
            accountService.createAccount(account.documentNumber());
            verify(accountRepository, times(1)).save(ArgumentMatchers.any(AccountEntity.class));
        }
    }

    @Nested
    @DisplayName("Not so happy path")
    class NotSoHappyPath {

        @ParameterizedTest
        @DisplayName("Should not save an account without a document number")
        @CsvSource({
                //documentNumber
                "$NULL"
        })
        void shouldNotSaveAnAccountWithoutADocumentNumber(@AggregateWith(AccountAggregator.class) Account account) {

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> accountService.createAccount(account.documentNumber()),
                    "ValidationException expected to be thrown"
            );
            Assertions.assertEquals("Document number must be provided.", exception.getMessage());

            verify(accountRepository, times(0)).save(ArgumentMatchers.any(AccountEntity.class));
        }

        @ParameterizedTest
        @DisplayName("Should not save an account with an invalid document number")
        @CsvSource({
                //documentNumber
                "0",
                "-1"
        })
        void shouldNotSaveAnAccountWithAnInvalidDocumentNumber(@AggregateWith(AccountAggregator.class) Account account) {

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> accountService.createAccount(account.documentNumber()),
                    "ValidationException expected to be thrown"
            );
            Assertions.assertEquals("Document number is invalid.", exception.getMessage());

            verify(accountRepository, times(0)).save(ArgumentMatchers.any(AccountEntity.class));
        }

        @ParameterizedTest
        @DisplayName("Should not save an account if there is already an account with its document number")
        @CsvSource({
                //documentNumber
                "123",
                "12345"
        })
        void shouldNotSaveAnAccountIfThereIsAlreadyAnotherAccountWithItsDocumentNumber(@AggregateWith(AccountAggregator.class) Account account) {

            lenient().when(accountRepository.existsByDocumentNumber(ArgumentMatchers.anyLong())).thenReturn(true);

            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> accountService.createAccount(account.documentNumber()),
                    "ValidationException expected to be thrown"
            );
            Assertions.assertEquals("There is an account that is already registered with this document number.", exception.getMessage());

            verify(accountRepository, times(0)).save(ArgumentMatchers.any(AccountEntity.class));
        }
    }

}