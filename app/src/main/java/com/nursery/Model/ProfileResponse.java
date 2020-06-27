package com.nursery.Model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @SerializedName("userName")
    String name;

    @SerializedName("MobileNumber")
    String MobileNumber;

    @SerializedName("securityPin")
    String securityPin;

    @SerializedName("shopStatus")
    String shopStatus;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }

    public String getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(String shopStatus) {
        this.shopStatus = shopStatus;
    }
}
