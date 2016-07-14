package com.github.lhsm.service;

import com.github.lhsm.data.AccountRepository;
import com.github.lhsm.data.TransactionRepository;
import com.github.lhsm.domain.Account;
import com.github.lhsm.domain.State;
import com.github.lhsm.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Transaction transfer(long withdrawalAccount, long depositAccount, BigInteger amount) {
        Account wAccount = getAccount(withdrawalAccount);

        if (wAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }


        Transaction transaction = new Transaction(
                wAccount,
                getAccount(depositAccount),
                amount,
                State.PROCESSING.name(),
                new Date()
        );

        return transactionRepository.save(transaction);
    }


    private Account getAccount(long accountId) {
        Account account = accountRepository.getOne(accountId);

        if (account == null) {
            throw new ResourceNotFoundException("Account with id " + accountId + " not found");
        }

        return account;
    }

}
