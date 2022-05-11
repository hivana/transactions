package io.pismo.transactions.configuration.generic;

public interface GenericResponseMapper<M, R> {

    M toModel(R response);

    R fromModel(M model);

}
