package com.web3.repository;

import com.web3.entity.CreateWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateWalletRepository extends JpaRepository<CreateWallet,String > {
    boolean existsByLabel(String label);
}
