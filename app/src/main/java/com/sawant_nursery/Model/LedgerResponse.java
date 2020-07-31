package com.sawant_nursery.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LedgerResponse {

    @SerializedName("purchase_id")
    @Expose
    private String purchaseId;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("order_amount")
    @Expose
    private String orderAmount;
    @SerializedName("order_pending_amount")
    @Expose
    private String orderPendingAmount;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderPendingAmount() {
        return orderPendingAmount;
    }

    public void setOrderPendingAmount(String orderPendingAmount) {
        this.orderPendingAmount = orderPendingAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
