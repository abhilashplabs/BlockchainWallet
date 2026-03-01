package com.web3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "transactio_registry")
public class TransactionRecord {
    @Id
    private String txnHash;
    private String fromAddress;
    private String toAddress;
    private BigDecimal value;
    private Long blockNumber;
    private LocalDateTime indexedAt;

    public TransactionRecord(String txnHash, String fromAddress, String toAddress, BigDecimal value, Long blockNumber, LocalDateTime indexedAt) {
        this.txnHash = txnHash;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.value = value;
        this.blockNumber = blockNumber;
        this.indexedAt = indexedAt;
    }

    public TransactionRecord() {
    }

    public String getTxnHash() {
        return txnHash;
    }

    public void setTxnHash(String txnHash) {
        this.txnHash = txnHash;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public LocalDateTime getIndexedAt() {
        return indexedAt;
    }

    public void setIndexedAt(LocalDateTime indexedAt) {
        this.indexedAt = indexedAt;
    }
}
