package com.web3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BlockchainService {

    @Autowired
    private Web3j web3j;

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

    public void storeAddress(String label, String address){
        walletStorage.put(label,address);
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

        return receipt.getTransactionHash();
    }

}
