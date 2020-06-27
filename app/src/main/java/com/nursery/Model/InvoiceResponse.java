package com.nursery.Model;

import com.google.gson.annotations.SerializedName;

public class InvoiceResponse {

    @SerializedName("final_order_id")
    String final_order_id;

    @SerializedName("total_amount")
    String total_amount;

    @SerializedName("order_number")
    String order_number;

    @SerializedName("order_date")
    String order_date;



    public String getFinal_order_id() {
        return final_order_id;
    }

    public void setFinal_order_id(String final_order_id) {
        this.final_order_id = final_order_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }
}
