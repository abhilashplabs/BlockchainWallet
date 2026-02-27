package com.web3.controller;

import com.web3.dto.CreateWalletRequest;
import com.web3.dto.WalletBalanceResponse;
import com.web3.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class BlockchainController {

    @Autowired
    BlockchainService blockchainService;

    @GetMapping("/balance/{address}")
    public ResponseEntity<WalletBalanceResponse> getWalletBalance(@PathVariable String address){
        try{
            BigDecimal balance = blockchainService.getBalanceInEther(address);

            WalletBalanceResponse response = new WalletBalanceResponse(
                    address,
                    balance,
                    "ETH",
                    System.currentTimeMillis()
            );

            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    public  ResponseEntity<?> createWallet(@RequestBody CreateWalletRequest request){
        try{
            Map<String,String> result = blockchainService.createAndLoadAccount(request.password());
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error"+ e.getMessage());
        }
    }
}
