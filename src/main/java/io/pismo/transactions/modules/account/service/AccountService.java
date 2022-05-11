package io.pismo.transactions.modules.account.service;

import io.pismo.transactions.configuration.exception.ValidationException;
import io.pismo.transactions.modules.account.dto.Account;
import io.pismo.transactions.modules.account.persistence.mapper.AccountEntityMapper;
import io.pismo.transactions.modules.account.persistence.repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountEntityMapper accountEntityMapper;

    public AccountService(AccountRepository accountRepository, AccountEntityMapper accountEntityMapper) {
        this.accountRepository = accountRepository;
        this.accountEntityMapper = accountEntityMapper;
    }

    @Transactional
    public Account createAccount(Long documentNumber) {
        Account accountCandidate = new Account(null, documentNumber);
        validate(accountCandidate);
        return accountEntityMapper.toModel(accountRepository.save(accountEntityMapper.fromModel(accountCandidate)));
    }

    private void validate(Account account) {
        inputValidate(account);

        if (accountRepository.existsByDocumentNumber(account.documentNumber())) {
            throw new ValidationException("There is an account that is already registered with this document number.");
        }
    }

    public Account getById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Account id must be provided.");
        }
        return accountRepository.findById(id)
                .map(accountEntityMapper::toModel)
                .orElse(null);
    }

    public boolean existsById(Long id) {
        return accountRepository.existsById(id);
    }

    private void inputValidate(Account account) {

        if (account == null) {
            throw new ValidationException("Account must be provided.");
        }

        if (account.documentNumber() == null) {
            throw new ValidationException("Document number must be provided.");
        }

        if (account.documentNumber() <= 0) {
            throw new ValidationException("Document number is invalid.");
        }

    }
}
