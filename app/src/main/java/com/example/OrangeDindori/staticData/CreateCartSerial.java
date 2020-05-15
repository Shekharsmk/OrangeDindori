package com.example.OrangeDindori.staticData;

import java.io.Serializable;

public class CreateCartSerial extends ProductSerial implements Serializable {
    int amount;
    int totalCost;
    public CreateCartSerial() {
        super();
        this.amount = amount;
        this.totalCost = totalCost;
    }
    public CreateCartSerial(int amount, int totalCost) {
        super();
        this.amount = amount;
        this.totalCost = totalCost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }



    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
