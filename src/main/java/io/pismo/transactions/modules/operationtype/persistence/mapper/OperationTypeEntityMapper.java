package io.pismo.transactions.modules.operationtype.persistence.mapper;

import io.pismo.transactions.configuration.generic.GenericEntityMapper;
import io.pismo.transactions.modules.operationtype.dto.OperationType;
import io.pismo.transactions.modules.operationtype.persistence.entity.OperationTypeEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(builder = @Builder(disableBuilder = true),
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface OperationTypeEntityMapper extends GenericEntityMapper<OperationType, OperationTypeEntity> {
}
