package io.pismo.transactions.parameterized;

import io.pismo.transactions.modules.transaction.dto.Transaction;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionAggregator extends AbstractAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
        return new Transaction(
                null,
                FakeFactory.buildAccountWithId(getNullOrValue(accessor, 2, Long.class)),
                FakeFactory.buildOperationTypeWithId(getNullOrValue(accessor, 3, Integer.class)),
                (BigDecimal) getNullOrValue(accessor, 0, BigDecimal.class),
                (LocalDateTime) getNullOrValue(accessor, 1, LocalDateTime.class)
        );
    }

}
