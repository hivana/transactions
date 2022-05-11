package io.pismo.transactions.modules.operationtype.persistence.entity;

import javax.persistence.*;

@Entity
@Table(schema = "transactions", name = "operation_type")
public class OperationTypeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(generator = "gen_operation_type_id", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "gen_operation_type_id", schema = "transactions", sequenceName = "seq_operation_type_id", allocationSize = 1)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
