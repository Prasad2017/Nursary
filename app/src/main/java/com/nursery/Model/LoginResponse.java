package com.nursery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("userId")
    @Expose
    String userId;

    @SerializedName("securityPin")
    @Expose
    String securityPin;

    @SerializedName("productId")
    @Expose
    String productId;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
