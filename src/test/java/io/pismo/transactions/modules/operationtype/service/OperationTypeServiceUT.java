package io.pismo.transactions.modules.operationtype.service;

import io.pismo.transactions.modules.operationtype.persistence.repository.OperationTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationTypeServiceUT {

    @Mock
    private OperationTypeRepository operationTypeRepository;
    private AutoCloseable closeable;
    private OperationTypeService operationTypeService;

    @BeforeEach
    public void beforeEach() {
        this.closeable = MockitoAnnotations.openMocks(this);
        this.operationTypeService = new OperationTypeService(operationTypeRepository);
    }

    @AfterEach
    void afterEach() throws Exception {
        this.closeable.close();
    }

    @Test
    @DisplayName("Should return the existence of an operation type")
    void shouldReturnTheExistenceOfAnOperationType() {
        lenient().when(operationTypeRepository.existsById(99)).thenReturn(true);
        assertTrue(operationTypeService.existsById(99));
        verify(operationTypeRepository, times(1)).existsById(99);

        lenient().when(operationTypeRepository.existsById(88)).thenReturn(false);
        assertFalse(operationTypeService.existsById(88));
        verify(operationTypeRepository, times(1)).existsById(88);
    }

}