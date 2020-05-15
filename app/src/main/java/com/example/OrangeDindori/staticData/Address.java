package com.example.OrangeDindori.staticData;

public class Address {
    private String address,city,landmark,name,phoneNo,pincode;
    private boolean primary;

    public Address() {
    }

    public Address(String address, String city, String landmark, String name, String phoneNo, String pincode,boolean primary) {
        this.address = address;
        this.city = city;
        this.landmark = landmark;
        this.name = name;
        this.phoneNo = phoneNo;
        this.pincode = pincode;
        this.primary = primary;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
