package com.example.OrangeDindori.staticData;

public class ItemCompanyOrder {
    private String url,productName,description;
    private int mrpPrice,discountPrice,amount,totalCost;
    private long itemID;

    public ItemCompanyOrder() {
    }

    public ItemCompanyOrder(String url, String productName, String description, long itemID, int mrpPrice, int discountPrice, int amount, int totalCost) {
        this.url = url;
        this.productName = productName;
        this.description = description;
        this.itemID = itemID;
        this.mrpPrice = mrpPrice;
        this.discountPrice = discountPrice;
        this.amount = amount;
        this.totalCost = totalCost;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMrpPrice() {
        return mrpPrice;
    }

    public void setMrpPrice(int mrpPrice) {
        this.mrpPrice = mrpPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
