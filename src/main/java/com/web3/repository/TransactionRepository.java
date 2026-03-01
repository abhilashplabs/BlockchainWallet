package com.web3.repository;

import com.web3.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionRecord,String> {

    List<TransactionRecord> findByFromAddressOrToAddress(String from, String to);
}
