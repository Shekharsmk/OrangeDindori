package com.example.OrangeDindori.staticData;

import java.io.Serializable;


public class ProductSerial implements Serializable {
    private String url,productName,description,expiryDate;
    private int mrpPrice,discountPrice,discountPercentage;
    private long itemID;

    public ProductSerial() {
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getUrl() {
        return url;
    }

    public String getProductName() {
        return productName;
    }

    public int getMrpPrice() {
        return mrpPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getExpiryDate() {
        return expiryDate;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setMrpPrice(int mrpPrice) {
        this.mrpPrice = mrpPrice;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
