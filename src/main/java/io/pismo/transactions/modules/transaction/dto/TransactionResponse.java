package io.pismo.transactions.modules.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(@JsonProperty("transaction_id") Long transactionId,
                                  @JsonProperty("account_id") Long accountId,
                                  @JsonProperty("operation_type_id") Long operationTypeId,
                                  BigDecimal amount,
                                  @JsonProperty("event_date") LocalDateTime eventDate) {
}
