package io.pismo.transactions.modules.operationtype.persistence.repository;

import io.pismo.transactions.modules.operationtype.persistence.entity.OperationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationTypeEntity, Integer> {
}
