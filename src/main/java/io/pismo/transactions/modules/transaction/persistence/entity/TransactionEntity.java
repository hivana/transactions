package io.pismo.transactions.modules.transaction.persistence.entity;

import io.pismo.transactions.modules.account.persistence.entity.AccountEntity;
import io.pismo.transactions.modules.operationtype.persistence.entity.OperationTypeEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "transactions", name = "transaction")
public class TransactionEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(generator = "gen_transaction_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "gen_transaction_id", schema = "transactions", sequenceName = "seq_transaction_id", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name="account_id", nullable = false, updatable = false)
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name="operation_type_id", nullable = false, updatable = false)
    private OperationTypeEntity operationType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "event_date", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime eventDate = LocalDateTime.now();

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    private LocalDateTime lastModifiedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public OperationTypeEntity getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationTypeEntity operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
