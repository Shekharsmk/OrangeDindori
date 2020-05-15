package com.example.OrangeDindori.staticData;

import java.util.Date;

public class OrderDetails {
    private String deliveryDate,dispatchDate,OrderDate,paymentMethod,paymentstatus,status,odextra1="odextra1",
            odextra2="odextra2",odextra3="odextra3",odextra4="odextra4";

    public OrderDetails() {
    }

    public OrderDetails(String deliveryDate, String dispatchDate, String orderDate, String paymentMethod,
                        String paymentstatus, String status, String odextra1, String odextra2, String odextra3, String odextra4) {
        this.deliveryDate = deliveryDate;
        this.dispatchDate = dispatchDate;
        OrderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.paymentstatus = paymentstatus;
        this.status = status;
        this.odextra1 = odextra1;
        this.odextra2 = odextra2;
        this.odextra3 = odextra3;
        this.odextra4 = odextra4;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOdextra1() {
        return odextra1;
    }

    public void setOdextra1(String odextra1) {
        this.odextra1 = odextra1;
    }

    public String getOdextra2() {
        return odextra2;
    }

    public void setOdextra2(String odextra2) {
        this.odextra2 = odextra2;
    }

    public String getOdextra3() {
        return odextra3;
    }

    public void setOdextra3(String odextra3) {
        this.odextra3 = odextra3;
    }

    public String getOdextra4() {
        return odextra4;
    }

    public void setOdextra4(String odextra4) {
        this.odextra4 = odextra4;
    }
}

