package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;

public class CartResponse {

    @SerializedName("cart_id")
    String cart_id;

    @SerializedName("product_id")
    String product_id;

    @SerializedName("product_name")
    String product_name;

    @SerializedName("product_price")
    String product_price;

    @SerializedName("product_size")
    String product_size;

    @SerializedName("product_bag_size")
    String product_bag_size;

    @SerializedName("product_image")
    String product_image;

    @SerializedName("product_quantity")
    String product_quantity;

    @SerializedName("customer_id")
    String customer_id;

    @SerializedName("sub_total")
    String sub_total;

    @SerializedName("cartQuantity")
    String quantity;

    @SerializedName("subAmount")
    String subAmount;

    @SerializedName("cgst")
    String cgst;

    @SerializedName("sgst")
    String sgst;

    @SerializedName("igst")
    String igst;



    //All getter and setter methods.....

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(String subAmount) {
        this.subAmount = subAmount;
    }

    public String getProduct_bag_size() {
        return product_bag_size;
    }

    public void setProduct_bag_size(String product_bag_size) {
        this.product_bag_size = product_bag_size;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }


}
