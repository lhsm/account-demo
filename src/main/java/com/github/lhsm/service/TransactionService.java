package com.github.lhsm.service;

import com.github.lhsm.domain.Transaction;

import java.math.BigInteger;

public interface TransactionService {

    Transaction transfer(long withdrawalAccount, long depositAccount, BigInteger amount);
}
