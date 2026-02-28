# 🚀 EVM Blockchain Wallet & Transfer API

A professional **Spring Boot** application designed to interface with **Ethereum-compatible blockchains** (EVM). This project demonstrates the full lifecycle of a non-custodial wallet—from cryptographic key generation to local transaction signing and real-time balance tracking.

---

## 🛠️ Tech Stack
* **Backend:** Java 17, Spring Boot 3.2.x
* **Blockchain Bridge:** Web3j 4.10.x (Java SDK for Ethereum)
* **Local Network:** Hardhat / Ganache
* **Tools:** Maven, Postman, Bouncy Castle (Cryptography)

---

## ✨ Key Features

### 1. Wallet & Account Management
* **Secure Generation:** Creates Ethereum KeyStore (V3) files encrypted with a user-defined password.
* **BIP-39 Support:** Generates 12-word mnemonic seed phrases for industry-standard recovery.
* **Local Loading:** Loads credentials into a `Credentials` object to sign transactions locally, ensuring private keys never leave the server environment.

### 2. Live Blockchain Interaction
* **Real-time Balances:** Fetches live ETH/Native token balances from any RPC-compatible node.
* **Address Book:** A thread-safe `ConcurrentHashMap` storage system to "Watch" multiple addresses.
* **Account Discovery:** Identifies if an address is an **EOA** (User Wallet) or a **Smart Contract**.

### 3. Transaction Engine
* **Local Signing:** Signs transactions using private keys (e.g., from Ganache) and broadcasts them to the network.
* **Automated Gas Management:** Uses Web3j utilities to estimate gas limits and current market gas prices.
* **Confirmation Tracking:** Returns transaction hashes and waits for block inclusion receipts.

---

## 🚀 Getting Started

### 1. Prerequisites
* **Java 17** installed on your Mac.
* **Hardhat** or **Ganache** running locally.
* **Postman** for API testing.

### 2. Setup Local Blockchain
In a separate terminal, start your local node:
```bash
# Using Hardhat
npx hardhat node
```
### 3. Build
git clone [https://github.com/abhilashplabs/BlockchainWallet.git](https://github.com/abhilashplabs/BlockchainWallet.git)
cd BlockchainWallet
./mvnw clean spring-boot:run

The application will start on http://localhost:8080.

### 4. API Reference
Method,Endpoint,Description


|POST,/api/wallet/create,Generates and saves a new encrypted wallet file.|

|POST,/api/wallet/store,Adds an address and label to the internal watchlist.|
|GET,/api/wallet/balances,Loads live balances for all stored addresses.|

### 5. Blockchain Operations
Method,Endpoint,Description
GET,/api/wallet/balance/{address},Get balance for a specific address.
POST,/api/wallet/transfer,Transfers ETH from a source private key to a destination.
GET,/api/wallet/nonce/{address},Gets the next valid transaction nonce.

### 6. Engineering Best Practices

DTO Pattern: Used Java Records for clean, immutable data transfer between the API and Service layers.

Thread Safety: Utilized ConcurrentHashMap to manage application state safely across multiple concurrent requests.

Defensive Coding: Implemented structured error handling to catch RPC failures and null parameters gracefully.

Security Mindset: Designed the architecture to support local signing, preventing private key exposure over the network.


