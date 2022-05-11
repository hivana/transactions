package io.pismo.transactions.modules.transaction.service;

import io.pismo.transactions.configuration.exception.ValidationException;
import io.pismo.transactions.modules.account.service.AccountService;
import io.pismo.transactions.modules.transaction.dto.Transaction;
import io.pismo.transactions.modules.transaction.persistence.mapper.TransactionEntityMapper;
import io.pismo.transactions.modules.transaction.persistence.repository.TransactionRepository;
import io.pismo.transactions.modules.operationtype.service.OperationTypeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class TransactionService {

    private static final int PAYMENT = 4;

    private final TransactionRepository transactionRepository;
    private final TransactionEntityMapper transactionEntityMapper;
    private final AccountService accountService;
    private final OperationTypeService operationTypeService;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionEntityMapper transactionEntityMapper,
                              AccountService accountService,
                              OperationTypeService operationTypeService) {
        this.transactionRepository = transactionRepository;
        this.transactionEntityMapper = transactionEntityMapper;
        this.accountService = accountService;
        this.operationTypeService = operationTypeService;
    }

    @Transactional
    public Transaction createTransaction(Transaction transactionCandidate) {
        validate(transactionCandidate);
        Transaction transaction = sanitize(transactionCandidate);
        return transactionEntityMapper.toModel(
                transactionRepository.save(
                        transactionEntityMapper.fromModel(transaction)
                )
        );
    }

    private Transaction sanitize(Transaction transactionCandidate) {
        if (isPaymentAndAmountIsNegative(transactionCandidate) || isNotPaymentAndAmountIsPositive(transactionCandidate)) {
            return Transaction.buildTransactionWithAmount(transactionCandidate, transactionCandidate.amount().negate());
        }
        return transactionCandidate;
    }

    private void validate(Transaction transaction) {
        inputValidate(transaction);

        if (!accountService.existsById(transaction.account().id())) {
            throw new ValidationException("Account not found.");
        }

        if (!operationTypeService.existsById(transaction.operationType().id())) {
            throw new ValidationException("Operation type not found.");
        }
    }

    private void inputValidate(Transaction transaction) {

        if (transaction == null) {
            throw new ValidationException("Invalid input.");
        }

        if (transaction.account() == null || transaction.account().id() == null) {
            throw new ValidationException("Account must be provided.");
        }

        if (transaction.operationType() == null || transaction.operationType().id() == null) {
            throw new ValidationException("Operation type must be provided.");
        }

        if (transaction.amount() == null || BigDecimal.ZERO.equals(transaction.amount())) {
            throw new ValidationException("Amount must be provided.");
        }

    }

    private static boolean isNotPaymentAndAmountIsPositive(final Transaction transaction) {
        return transaction.operationType().id() != PAYMENT && transaction.amount().compareTo(BigDecimal.ZERO) > 0;
    }

    private static boolean isPaymentAndAmountIsNegative(final Transaction transaction) {
        return transaction.operationType().id() == PAYMENT && transaction.amount().compareTo(BigDecimal.ZERO) < 0;
    }

}
