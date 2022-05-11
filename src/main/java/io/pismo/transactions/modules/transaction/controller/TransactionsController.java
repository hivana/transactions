package io.pismo.transactions.modules.transaction.controller;

import io.pismo.transactions.modules.transaction.dto.TransactionRequest;
import io.pismo.transactions.modules.transaction.dto.TransactionResponse;
import io.pismo.transactions.modules.transaction.dto.mapper.TransactionResponseMapper;
import io.pismo.transactions.modules.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionService transactionService;
    private final TransactionResponseMapper transactionResponseMapper;

    private final Logger log = LoggerFactory.getLogger(TransactionsController.class);

    public TransactionsController(TransactionService transactionService,
                                  TransactionResponseMapper transactionResponseMapper) {
        this.transactionService = transactionService;
        this.transactionResponseMapper = transactionResponseMapper;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> save(@RequestBody TransactionRequest request) {

        log.info("request for creating a new transaction");

        TransactionResponse response = transactionResponseMapper.fromModel(
                transactionService.createTransaction(
                        transactionResponseMapper.fromRequest(request)
                )
        );

        log.info("transaction successfully created");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
