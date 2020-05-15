package com.example.OrangeDindori.staticData;

import android.os.Parcelable;

import java.io.Serializable;

public class CreateCart extends ProductSerial implements Serializable {
    int amount;
    int totalCost;

    public CreateCart() {
        this.amount = amount;
        this.totalCost = totalCost;
    }

    public CreateCart(int amount, int totalCost) {
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
