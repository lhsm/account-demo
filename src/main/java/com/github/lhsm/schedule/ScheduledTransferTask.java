package com.github.lhsm.schedule;

import com.github.lhsm.data.AccountRepository;
import com.github.lhsm.data.TransactionRepository;
import com.github.lhsm.domain.Account;
import com.github.lhsm.domain.State;
import com.github.lhsm.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Component
public class ScheduledTransferTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduledTransferTask.class);

    @Value("${scheduler.transferTask.pageSize}")
    private int pageSize;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Scheduled(fixedRateString = "${scheduler.transferTask.fixedRate}")
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void transfer() {
        List<Transaction> processing = transactionRepository.findByState(State.PROCESSING.name(), new PageRequest(0, pageSize));

        processing.stream().forEach(this::doTransfer);
    }

    private void doTransfer(Transaction transaction) {
        changeBalance(transaction);

        transactionRepository.saveAndFlush(transaction);
    }

    private void changeBalance(Transaction transaction) {
        BigInteger amount = transaction.getAmount();

        Account wAccount = accountRepository.getOne(transaction.getWithdrawalAccountId());

        Account dAccount = accountRepository.getOne(transaction.getDepositAccountId());

        if (wAccount == null || dAccount == null) {
            transaction.setState(State.FAILED.name());
            return;
        }

        if (wAccount.getBalance().compareTo(amount) < 0) {
            transaction.setState(State.FAILED.name());
            return;
        }

        accountRepository.saveAndFlush(new Account(wAccount.getId(), wAccount.getBalance().add(amount.negate())));

        accountRepository.saveAndFlush(new Account(dAccount.getId(), dAccount.getBalance().add(amount)));

        transaction.setState(State.DONE.name());
    }


}
