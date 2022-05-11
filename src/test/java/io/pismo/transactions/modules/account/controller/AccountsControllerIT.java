package io.pismo.transactions.modules.account.controller;

import io.pismo.transactions.configuration.MappingConfiguration;
import io.pismo.transactions.configuration.exception.ValidationException;
import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.account.service.AccountService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        AccountsController.class,
        MappingConfiguration.class
})
class AccountsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

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
            lenient().when(accountService.createAccount(ArgumentMatchers.anyLong())).thenReturn(new Account(1L, 123456789L));
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                    .content("""
                            {
                               "document_number": 123456789
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

        @Test
        @DisplayName("Should return 200 as it gets a valid input for get method")
        void shouldReturn200AsItGetsAValidInputForGetMethod() throws Exception {
            lenient().when(accountService.getById(1L)).thenReturn(new Account(1L, 123456789L));
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                    .get("/accounts/{id}", 1L)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isOk(),
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
            lenient().when(accountService.createAccount(null)).thenThrow(new ValidationException("Document number must be provided."));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                    .content("""
                            {
                               "document_number": ""
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

        @Test
        @DisplayName("Should return 404 as it gets an input for a non existent resource via get method")
        void shouldReturn404AsItGetsAnInputForANonExistentResourceViaGetMethod() throws Exception {
            lenient().when(accountService.getById(999L)).thenReturn(null);

            mockMvc.perform(MockMvcRequestBuilders
                    .get("/accounts/{id}", 999)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(
                            status().isNotFound()
                    );
        }

        @Test
        @DisplayName("Should return 400 as it gets an invalid input for a get method")
        void shouldReturn400AsItGetsAnInvalidInputForAGetMethod() throws Exception {
            lenient().when(accountService.getById(0L)).thenThrow(new ValidationException("Account id must be provided."));

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                    .get("/accounts/{id}", 0)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").value("400"),
                            jsonPath("$.message").value("Account id must be provided.")
                    ).andReturn();

            String resultString = result.getResponse().getContentAsString();
            assertNotNull(resultString);
        }

    }


}