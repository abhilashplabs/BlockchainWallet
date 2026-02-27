package com.web3.dto;

import java.math.BigDecimal;

public record WalletBalanceResponse(
        String address,
        BigDecimal balance,
        String unit,
        long timestamp
) { }
