package io.pismo.transactions.modules.transaction.controller;

import io.pismo.transactions.configuration.MappingConfiguration;
import io.pismo.transactions.configuration.exception.ValidationException;
import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.operationtype.dto.OperationType;
import io.pismo.transactions.modules.transaction.dto.Transaction;
import io.pismo.transactions.modules.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        TransactionsController.class,
        MappingConfiguration.class
})
class TransactionsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Happy path")
    class HappyPath {

        @Test
        @DisplayName("Should return 201 as it gets a valid input for post method")
        void shouldReturn201AsItGetsAValidInputForPostMethod() throws Exception {
            Transaction transactionCandidate = new Transaction(null, new Account(1L, new Random().nextLong()), new OperationType(4, "PAGAMENTO"), new BigDecimal(1), null);
            lenient().when(transactionService.createTransaction(ArgumentMatchers.any(Transaction.class))).thenReturn(transactionCandidate);
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                    .content("""
                            {
                                "account_id": 1,
                                "operation_type_id": 4,
                                "amount": 1
                             }
                            """)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.account_id").value("1"),
                            jsonPath("$.document_number").value("123456789")
                    ).andReturn();

            String resultString = result.getResponse().getContentAsString();
            assertNotNull(resultString);
        }

    }

    @Nested
    @DisplayName("Not so happy path")
    class NotSoHappyPath {

        @Test
        @DisplayName("Should return 400 as it gets an invalid input for a post method")
        void shouldReturn400AsItGetsAnInvalidInputForAPostMethod() throws Exception {
            lenient().when(transactionService.createTransaction(null)).thenThrow(new ValidationException("Document number must be provided."));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                    .content("""
                            {
                                "account_id": 1,
                                "operation_type_id": 4,
                                "amount": 1
                             }
                            """)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").value("400"),
                            jsonPath("$.message").value("Document number must be provided.")
                    ).andReturn();

            String resultString = result.getResponse().getContentAsString();
            assertNotNull(resultString);
        }

    }
}