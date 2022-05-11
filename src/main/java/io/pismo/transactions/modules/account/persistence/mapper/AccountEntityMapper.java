package io.pismo.transactions.modules.account.persistence.mapper;

import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.account.persistence.entity.AccountEntity;
import io.pismo.transactions.configuration.generic.GenericEntityMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(builder = @Builder(disableBuilder = true),
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AccountEntityMapper extends GenericEntityMapper<Account, AccountEntity> {
}
