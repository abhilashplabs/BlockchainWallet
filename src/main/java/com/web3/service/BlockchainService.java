package com.web3.service;

import com.web3.entity.CreateWallet;
import com.web3.entity.TransactionRecord;
import com.web3.repository.CreateWalletRepository;
import com.web3.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BlockchainService {

    @Autowired
    private Web3j web3j;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CreateWalletRepository createWalletRepository;


    public List<TransactionRecord> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<TransactionRecord> getTransactionsByAddress(String address) {
        // Fetches transactions where the address is either the sender or receiver
        return transactionRepository.findByFromAddressOrToAddress(address, address);
    }

    public BigDecimal getBalanceInEther(String walletAdddress) throws Exception{
        EthGetBalance ethGetBalance = web3j
                .ethGetBalance(walletAdddress, DefaultBlockParameterName.LATEST)
                .send();
        return Convert.fromWei(ethGetBalance.getBalance().toString(),Convert.Unit.ETHER);
    }

//    public String createWallet(String password) throws Exception{
//        String tempDir = System.getProperty("java.io.tmpdir");
//
//        String fileName = WalletUtils.generateFullNewWalletFile(password,new File(tempDir));
//        return  "Wallet Created Successfully in: " + "/"+fileName;
//    }

    public Map<String,String> createAndLoadAccount(String password) throws Exception{
        String tempDir = System.getProperty("java.io.tmpdir");
        String fileName = WalletUtils.generateFullNewWalletFile(password, new File(tempDir));
        String filePath = tempDir + (tempDir.endsWith(File.separator) ? "" : File.separator) + fileName;

        Credentials credentials = WalletUtils.loadCredentials(password,filePath);
        Map<String,String> accountInfo = new HashMap<>();
        accountInfo.put("address",credentials.getAddress());
        accountInfo.put("filePath",filePath);

        return  accountInfo;
    }
    // Thread-safe in-memory storage for wallet addresses
    private final Map<String,String> walletStorage = new ConcurrentHashMap<>();

    public CreateWallet storeAddress(String label, String address){

        if (label == null || address == null) {
            throw new IllegalArgumentException("Label or Address is missing in the request");
        }

        if(createWalletRepository.existsByLabel(label)){
            throw new RuntimeException("A wallet with this label is already in use");
        }

        CreateWallet wallet = new CreateWallet(label, address);
        return createWalletRepository.save(wallet);
    }

    public Map<String, BigDecimal> getAllBalances() throws Exception{
        Map<String,BigDecimal> result = new HashMap<>();

        for (Map.Entry<String,String> entry : walletStorage.entrySet()){
            BigDecimal balance = getBalanceInEther(entry.getValue());
            result.put(entry.getKey() + " (" + entry.getValue() + ")", balance);
        }
        return result;
    }

    public String transferEth(String privateKey, String toAddress, BigDecimal amount) throws  Exception{

        Credentials credentials = Credentials.create(privateKey);

        TransactionReceipt receipt = Transfer.sendFunds(
                web3j,
                credentials,
                toAddress,
                amount,
                Convert.Unit.ETHER
        ).send();

        TransactionRecord transaction = new TransactionRecord();
        transaction.setTxnHash(receipt.getTransactionHash());
        transaction.setFromAddress(receipt.getFrom());
        transaction.setToAddress(receipt.getTo());
        transaction.setValue(amount);
        transaction.setBlockNumber(receipt.getBlockNumber().longValue());
        transaction.setIndexedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        return receipt.getTransactionHash();
    }

    public Map<String,BigDecimal> estimateGasPrice() throws Exception{
        EthGasPrice gasPrice = web3j.ethGasPrice().send();
        BigDecimal base = Convert.fromWei(gasPrice.getGasPrice().toString(),Convert.Unit.GWEI);

        Map<String,BigDecimal> estimate = new HashMap<>();
        estimate.put("SafeLow",base);
        estimate.put("Standard",base.multiply(BigDecimal.valueOf(12)).divide(BigDecimal.valueOf(10)));
        estimate.put("Fast",base.multiply(BigDecimal.valueOf(15)).divide(BigDecimal.valueOf(10)));
        return estimate;

    }

}
