package io.pismo.transactions.modules.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record AccountRequest(@JsonProperty("document_number") Long documentNumber) {
}
