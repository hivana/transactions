package io.pismo.transactions.modules.transaction.persistence.repository;

import io.pismo.transactions.modules.transaction.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
