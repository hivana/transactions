package io.pismo.transactions.modules.account.dto.mapper;

import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.account.dto.AccountResponse;
import io.pismo.transactions.configuration.generic.GenericResponseMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(builder = @Builder(disableBuilder = true),
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AccountResponseMapper extends GenericResponseMapper<Account, AccountResponse> {
}
