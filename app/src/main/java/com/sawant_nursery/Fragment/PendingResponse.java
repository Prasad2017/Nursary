package com.sawant_nursery.Fragment;

import com.google.gson.annotations.SerializedName;

public class PendingResponse {

    @SerializedName("pending_amount")
    private String pending_amount;

    public String getPending_amount() {
        return pending_amount;
    }

    public void setPending_amount(String pending_amount) {
        this.pending_amount = pending_amount;
    }
}
