package io.pismo.transactions.modules.transaction.dto;

import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.operationtype.dto.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(Long id, Account account, OperationType operationType, BigDecimal amount, LocalDateTime eventDate) {

    public static Transaction buildTransactionWithAmount(Transaction transaction, BigDecimal amount) {
        return new Transaction(null, transaction.account(), transaction.operationType(), amount, transaction.eventDate());
    }

}
