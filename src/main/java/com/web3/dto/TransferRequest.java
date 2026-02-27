package com.web3.dto;

import java.math.BigDecimal;

public record TransferRequest(
        String privateKey,
        String toAddress,
        BigDecimal amount
) {
}
