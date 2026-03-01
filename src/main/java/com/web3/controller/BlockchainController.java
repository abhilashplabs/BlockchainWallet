package com.web3.controller;

import com.web3.dto.CreateWalletRequest;
import com.web3.dto.StoreWalletRequest;
import com.web3.dto.TransferRequest;
import com.web3.dto.WalletBalanceResponse;
import com.web3.entity.CreateWallet;
import com.web3.entity.TransactionRecord;
import com.web3.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Controller
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

    @PostMapping("/createAndLoadAccount")
    public  ResponseEntity<?> createWallet(@RequestBody CreateWalletRequest request){
        try{
            Map<String,String> result = blockchainService.createAndLoadAccount(request.password());

            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error"+ e.getMessage());
        }
    }

    @PostMapping("/store")
    public ResponseEntity<CreateWallet> storeWallet(@RequestBody StoreWalletRequest request) {
        CreateWallet savedWallet = blockchainService.storeAddress(request.label(), request.address());
        return ResponseEntity.ok(savedWallet);
    }

    // 2. Load and show all balances
    @GetMapping("/load-all")
    public ResponseEntity<Map<String, BigDecimal>> loadAllBalances() {
        try {
            return ResponseEntity.ok(blockchainService.getAllBalances());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> SendFunds(@RequestBody TransferRequest request){
        try{
            String txnHash = blockchainService.transferEth(
                    request.privateKey(),
                    request.toAddress(),
                    request.amount()
            );
            return ResponseEntity.status(200).body("Success"+ txnHash);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Transaction Failed" + e.getMessage());
        }
    }

    @GetMapping("/estimateGasPrice")
    public Map<String, BigDecimal> estimate() throws Exception {
        Map<String,BigDecimal> gasPrice = blockchainService.estimateGasPrice();
        return gasPrice;
    }

    @QueryMapping
    public List<TransactionRecord> allTransactions() {
        return blockchainService.getAllTransactions();
    }

    // Maps to: accountActivity(address: String!): [Transaction]
    @QueryMapping
    public List<TransactionRecord> accountActivity(@Argument String address) {
        return blockchainService.getTransactionsByAddress(address);
    }
}
