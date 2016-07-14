package com.github.lhsm.service;

import com.github.lhsm.data.AccountRepository;
import com.github.lhsm.data.TransactionRepository;
import com.github.lhsm.domain.Account;
import com.github.lhsm.domain.State;
import com.github.lhsm.domain.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    TransactionService service = new TransactionServiceImpl();

    @Test
    public void transfer() throws Exception {
        BigInteger amount = new BigInteger("1");
        Account withdrawalAccount = new Account(1, amount);
        Account depositAccount = new Account(2, amount);
        Transaction transaction = new Transaction(withdrawalAccount, depositAccount, amount, State.PROCESSING.name(), new Date());
        transaction.setId(1L);

        when(accountRepository.getOne(withdrawalAccount.getId())).thenReturn(withdrawalAccount);
        when(accountRepository.getOne(depositAccount.getId())).thenReturn(depositAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = service.transfer(withdrawalAccount.getId(), depositAccount.getId(), amount);

        assertNotNull("Transaction should be not null", result);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getWithdrawalAccount(), result.getWithdrawalAccount());
        assertEquals(transaction.getDepositAccount(), result.getDepositAccount());
        assertEquals(transaction.getState(), result.getState());
    }

    @Test(expected = RuntimeException.class)
    public void transfer_insufficientBalance() throws Exception {
        BigInteger amount = new BigInteger("1");
        Account withdrawalAccount = new Account(1, new BigInteger("0"));
        Account depositAccount = new Account(2, amount);

        when(accountRepository.getOne(withdrawalAccount.getId())).thenReturn(withdrawalAccount);

        service.transfer(withdrawalAccount.getId(), depositAccount.getId(), amount);

        verify(accountRepository, never()).getOne(depositAccount.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void transfer_withdrawalAccountNotExists() throws Exception {
        BigInteger amount = new BigInteger("1");
        Account withdrawalAccount = new Account(1, amount);
        Account depositAccount = new Account(2, amount);

        when(accountRepository.getOne(withdrawalAccount.getId())).thenReturn(null);
        when(accountRepository.getOne(depositAccount.getId())).thenReturn(depositAccount);

        service.transfer(withdrawalAccount.getId(), depositAccount.getId(), amount);

        verify(accountRepository, never()).getOne(depositAccount.getId());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void transfer_depositAccountNotExists() throws Exception {
        BigInteger amount = new BigInteger("1");
        Account withdrawalAccount = new Account(1, amount);
        Account depositAccount = new Account(2, amount);

        when(accountRepository.getOne(withdrawalAccount.getId())).thenReturn(withdrawalAccount);
        when(accountRepository.getOne(depositAccount.getId())).thenReturn(null);

        service.transfer(withdrawalAccount.getId(), depositAccount.getId(), amount);

        verify(transactionRepository, never()).save(any(Transaction.class));
    }

}