package io.pismo.transactions.parameterized;

import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public abstract class AbstractAggregator implements ArgumentsAggregator {

    protected Object getNullOrValue(ArgumentsAccessor accessor, int index, Class type) {
        if ("$NULL".equals(accessor.getString(index))) {
            return null;
        }
        if ("$EMPTY".equals(accessor.getString(index))) {
            return "";
        }
        return accessor.get(index, type);
    }
}
