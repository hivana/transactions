package io.pismo.transactions.modules.transaction.persistence.mapper;

import io.pismo.transactions.configuration.generic.GenericEntityMapper;
import io.pismo.transactions.modules.account.persistence.mapper.AccountEntityMapper;
import io.pismo.transactions.modules.operationtype.persistence.mapper.OperationTypeEntityMapper;
import io.pismo.transactions.modules.transaction.dto.Transaction;
import io.pismo.transactions.modules.transaction.persistence.entity.TransactionEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = {AccountEntityMapper.class, OperationTypeEntityMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TransactionEntityMapper extends GenericEntityMapper<Transaction, TransactionEntity> {
}
