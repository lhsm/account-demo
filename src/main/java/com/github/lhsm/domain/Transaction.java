package com.github.lhsm.domain;


import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigInteger;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Account withdrawalAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    private Account depositAccount;

    private BigInteger amount;

    private String state;

    @CreatedDate
    private Date createDate;

    public Transaction() {
    }

    public Transaction(Account withdrawalAccount, Account depositAccount, BigInteger amount, String state, Date createDate) {
        this.withdrawalAccount = withdrawalAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.state = state;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public Account getWithdrawalAccount() {
        return withdrawalAccount;
    }

    public Account getDepositAccount() {
        return depositAccount;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public String getState() {
        return state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setWithdrawalAccount(Account withdrawalAccount) {
        this.withdrawalAccount = withdrawalAccount;
    }

    public void setDepositAccount(Account depositAccount) {
        this.depositAccount = depositAccount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getWithdrawalAccountId() {
        return withdrawalAccount.getId();
    }

    public Long getDepositAccountId() {
        return depositAccount.getId();
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("withdrawalAccount", withdrawalAccount)
                .add("depositAccount", depositAccount)
                .add("amount", amount)
                .add("state", state)
                .add("createDate", createDate)
                .toString();
    }

}
