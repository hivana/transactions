package io.pismo.transactions.configuration.generic;

public interface GenericEntityMapper<M, E> {

    M toModel(E entity);

    E fromModel(M model);

}
