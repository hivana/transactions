package io.pismo.transactions.parameterized;

import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.account.persistence.entity.AccountEntity;
import io.pismo.transactions.modules.operationtype.dto.OperationType;
import io.pismo.transactions.modules.operationtype.persistence.entity.OperationTypeEntity;
import io.pismo.transactions.modules.transaction.dto.Transaction;
import io.pismo.transactions.modules.transaction.persistence.entity.TransactionEntity;

import java.util.Objects;
import java.util.Random;

public class FakeFactory {

    /**
     * If the param id is not null,
     * returns a Account object populated with its id and fake data for all other fields,
     * otherwise, returns null
     *
     * @param id
     * */
    public static Account buildAccountWithId(Object id) {
        if (!Objects.isNull(id) && id instanceof Long idAccount) {
            return new Account(idAccount, new Random().nextLong());
        }
        return null;
    }

    /**
     * If the param id is not null,
     * returns a OperationType object populated with its id and the corresponding description,
     * otherwise, returns null
     *
     * @param id
     * */
    public static OperationType buildOperationTypeWithId(Object id) {
        if (!Objects.isNull(id) && id instanceof Integer idOperationType) {
            String description = switch (idOperationType) {
                        case 1 -> "COMPRA A VISTA";
                        case 2 -> "COMPRA PARCELADA";
                        case 3 -> "SAQUE";
                        case 4 -> "PAGAMENTO";
                        default -> "INVALID OPERATION TYPE";
                    };
            return new OperationType(idOperationType, description);
        }
        return null;
    }

    /**
     * Builds a TransactionEntity object from Transaction data
     *
     * @param transaction
     * */
    public static TransactionEntity buildTransactionEntityWithTransaction(Transaction transaction) {
        TransactionEntity transactionEntity = new TransactionEntity();
        if (transaction.account() != null) {
            transactionEntity.setAccount(new AccountEntity(transaction.account().id(), transaction.account().documentNumber()));
        }
        if (transaction.operationType() != null) {
            transactionEntity.setOperationType(new OperationTypeEntity());
            transactionEntity.getOperationType().setId(transaction.operationType().id());
            transactionEntity.getOperationType().setDescription(transaction.operationType().description());
        }
        transactionEntity.setAmount(transaction.amount());
        transactionEntity.setEventDate(transaction.eventDate());
        return transactionEntity;
    }
}
