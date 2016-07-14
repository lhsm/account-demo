package com.github.lhsm.web;

import com.github.lhsm.domain.Transaction;
import com.github.lhsm.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping(path = "/transfer")
public class TransferController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST)
    public Transaction transfer(@RequestParam("from") long withdrawalAccount, @RequestParam("to") long depositAccount, @RequestParam("amount") BigInteger amount) {
        return transactionService.transfer(withdrawalAccount, depositAccount, amount);
    }
}
