# 🚀 EVM Blockchain Indexer & Wallet Manager

A professional-grade **Blockchain Indexing Engine** built with **Spring Boot 3**. This project addresses the "RPC Latency" problem in Web3 by indexing Ethereum-based transactions into a local **PostgreSQL** instance and exposing them via a high-performance **GraphQL API**.



---

## 🛠 Tech Stack

* **Backend:** Java 21 / Spring Boot 3.2.x
* **Blockchain Interface:** Web3j (Ethereum Java Library)
* **API Layer:** Spring for GraphQL + REST Controllers
* **Persistence:** Spring Data JPA + PostgreSQL
* **Database Migrations:** Hibernate (Auto-update)
* **Dev Tools:** Maven, GraphiQL, Postman

---

## 🌟 Key Features

### 1. Reactive Indexing Pipeline
Uses **RxJava/Flowables** via Web3j to "listen" for new blocks in real-time. Transactions are automatically mapped to Java entities and persisted to a relational database.

### 2. High-Performance GraphQL API
By serving data from **PostgreSQL** instead of an Ethereum Node:
* **Zero Over-fetching:** Clients request only the fields they need (e.g., `txnHash` and `value`).
* **Speed:** Query resolution drops from seconds (RPC) to milliseconds (SQL).
* **Flexibility:** Includes specific queries like `accountActivity` to filter by wallet address.

### 3. Smart Wallet Management
* **Non-Custodial:** Local generation of encrypted Ethereum Keystore files.
* **Address Book:** Map complex Hex addresses to human-readable **Labels**.
* **Safe ID Generation:** Uses **UUID String IDs** to decouple database primary keys from blockchain data.

---

## 🚀 Getting Started

### Prerequisites
* **JDK 21** (Project uses Java 21 features like Records)
* **Maven 3.x**
* **PostgreSQL** (Running on port 5432)
* **Ethereum Node:** Hardhat node, Geth, or a provider like Infura/Alchemy.

### Configuration
Update `src/main/resources/application.properties`:
```properties

# Blockchain Node (e.g., Hardhat)
web3j.client-address=[http://127.0.0.1:8545](http://127.0.0.1:8545)

# PostgreSQL Database
spring.datasource.url=jdbc:postgresql://localhost:5432/blockchain_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

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
```

git clone [[https://github.com/abhilashplabs/BlockchainWallet.git](https://github.com/abhilashplabs/Blockchain-Wallet.git)](https://github.com/abhilashplabs/Blockchain-Wallet.git)
cd BlockchainWallet
./mvnw clean spring-boot:run
```

The application will start on http://localhost:8080.

### 4. API Reference

| Method | Endpoint  | Description  |
| ------- | --- | --- |
| POST | /api/wallet/create | Generates and saves a new encrypted wallet file. |
| POST | /api/wallet/store | Adds an address and label to the internal watchlist. |
| GET | /api/wallet/balances | Loads live balances for all stored addresses. |

### 5. Blockchain Operations
Method,Endpoint,Description
| Method | Endpoint  | Description  |
| ------- | --- | --- |
| GET | /api/wallet/balance/{address} | Get balance for a specific address. |
| POST | /api/wallet/transfer | Transfers ETH from a source private key to a destination. |
| GET | /api/wallet/estimateGasPrice| Estimate the gasprice for the transaction |

GraphQL (The Indexer)
Access the playground at: http://localhost:8080/graphiql
```
GraphQL
query {
  allTransactions {
    txnHash
    fromAddress
    value
    blockNumber
  }
}
```

### 6. Engineering Best Practices

DTO Pattern: Used Java Records for clean, immutable data transfer between the API and Service layers.

Thread Safety: Utilized ConcurrentHashMap to manage application state safely across multiple concurrent requests.

Defensive Coding: Implemented structured error handling to catch RPC failures and null parameters gracefully.

Security Mindset: Designed the architecture to support local signing, preventing private key exposure over the network.


