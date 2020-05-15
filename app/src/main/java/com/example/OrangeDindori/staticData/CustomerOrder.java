package com.example.OrangeDindori.staticData;

public class CustomerOrder {
    String orderID;

    public CustomerOrder() {
    }

    public CustomerOrder(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
