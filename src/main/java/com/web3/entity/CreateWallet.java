package com.web3.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "walletAddress")
public class CreateWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String label;

    private String address;

    public CreateWallet() {
    }

    public CreateWallet(String label, String address) {
        this.label = label;
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CreateWallet{" +
                "label='" + label + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
