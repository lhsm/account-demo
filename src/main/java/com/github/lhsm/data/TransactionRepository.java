package com.github.lhsm.data;

import com.github.lhsm.domain.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "transaction", path = "transaction")
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.withdrawalAccount.id = ?1 or t.depositAccount.id = ?1")
    List<Transaction> findByAccountId(@Param("id") Long id);

    List<Transaction> findByState(@Param("state") String state, Pageable pageable);

}
