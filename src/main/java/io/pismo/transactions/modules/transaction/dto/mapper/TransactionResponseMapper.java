package io.pismo.transactions.modules.transaction.dto.mapper;

import io.pismo.transactions.modules.transaction.dto.Transaction;
import io.pismo.transactions.modules.transaction.dto.TransactionRequest;
import io.pismo.transactions.modules.transaction.dto.TransactionResponse;
import org.mapstruct.*;

@Mapper(builder = @Builder(disableBuilder = true),
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TransactionResponseMapper {

    @Mapping(target = "account.id", source = "accountId")
    @Mapping(target = "operationType.id", source = "operationTypeId")
    @Mapping(target = "amount", source = "amount")
    Transaction fromRequest(TransactionRequest request);

    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "operationTypeId", source = "operationType.id")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "eventDate", source = "eventDate")
    TransactionResponse fromModel(Transaction transaction);
}
