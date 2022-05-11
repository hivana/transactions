package io.pismo.transactions.modules.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record TransactionRequest(@JsonProperty("account_id") Long accountId,
                                 @JsonProperty("operation_type_id") Long operationTypeId,
                                 BigDecimal amount)
{}
