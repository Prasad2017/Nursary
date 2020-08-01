package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AmountResponse {

    @SerializedName("subTotal")
    String subTotal;

    @SerializedName("gst")
    String gst;

    @SerializedName("totalAmount")
    String totalAmount;

    @SerializedName("productResponse")
    List<ProductResponse> productResponseList;


    //All getter and Setter method...


    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ProductResponse> getProductResponseList() {
        return productResponseList;
    }

    public void setProductResponseList(List<ProductResponse> productResponseList) {
        this.productResponseList = productResponseList;
    }
}
