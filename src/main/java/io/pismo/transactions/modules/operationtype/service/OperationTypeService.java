package io.pismo.transactions.modules.operationtype.service;

import io.pismo.transactions.modules.operationtype.persistence.repository.OperationTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class OperationTypeService {

    private final OperationTypeRepository operationTypeRepository;

    public OperationTypeService(OperationTypeRepository operationTypeRepository) {
        this.operationTypeRepository = operationTypeRepository;
    }

    public boolean existsById(Integer id) {
        return operationTypeRepository.existsById(id);
    }

}
