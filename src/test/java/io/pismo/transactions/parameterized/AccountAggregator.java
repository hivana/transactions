package io.pismo.transactions.parameterized;

import io.pismo.transactions.modules.account.dto.Account;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;

public class AccountAggregator extends AbstractAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
        return new Account(null, (Long) getNullOrValue(accessor, 0, Long.class));
    }
}
