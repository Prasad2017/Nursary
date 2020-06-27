package com.nursery.Model;

import com.google.gson.annotations.SerializedName;

public class BagSizeResponse {

    @SerializedName("bag_size_id")
    String sizeId;

    @SerializedName("bag_size")
    String sizeName;


    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }
}
