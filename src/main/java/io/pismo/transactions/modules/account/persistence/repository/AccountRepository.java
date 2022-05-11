package io.pismo.transactions.modules.account.persistence.repository;

import io.pismo.transactions.modules.account.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    boolean existsByDocumentNumber(Long documentNumber);

}
