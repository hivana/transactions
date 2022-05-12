package io.pismo.transactions.modules.account.controller;

import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.account.dto.AccountRequest;
import io.pismo.transactions.modules.account.dto.AccountResponse;
import io.pismo.transactions.modules.account.dto.mapper.AccountResponseMapper;
import io.pismo.transactions.modules.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountService accountService;
    private final AccountResponseMapper accountResponseMapper;

    private final Logger log = LoggerFactory.getLogger(AccountsController.class);

    public AccountsController(AccountService accountService, AccountResponseMapper accountResponseMapper) {
        this.accountService = accountService;
        this.accountResponseMapper = accountResponseMapper;
    }

    @Operation(summary = "for creating a new account")
    @PostMapping
    public ResponseEntity<AccountResponse> save(@RequestBody AccountRequest request) {

        log.info("request for creating a new account");

        AccountResponse response = accountResponseMapper.fromModel(accountService.createAccount(request.documentNumber()));

        log.info("account successfully created");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "for retrieving an account")
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> findById(@PathVariable(name = "id") Long id) {

        log.info("request for retrieving an account");

        Account account = accountService.getById(id);
        if (account != null) {
            AccountResponse response = accountResponseMapper.fromModel(account);

            log.info("account successfully retrieved");

            return ResponseEntity.ok(response);
        }

        log.info("account not found");

        return ResponseEntity.notFound().build();
    }


}
