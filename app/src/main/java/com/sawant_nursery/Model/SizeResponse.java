package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;

public class SizeResponse {

    @SerializedName("size_id")
    String sizeId;

    @SerializedName("product_size")
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
