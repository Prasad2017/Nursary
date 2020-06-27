package com.nursery.Model;

import com.google.gson.annotations.SerializedName;

public class CategoryResponse {

    @SerializedName("product_type_id")
    String categoryId;

    @SerializedName("product_type")
    String categoryType;

    @SerializedName("sub_type_id_pk")
    String sub_type_id_pk;

    @SerializedName("sub_type_name")
    String sub_type_name;



    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getSub_type_id_pk() {
        return sub_type_id_pk;
    }

    public void setSub_type_id_pk(String sub_type_id_pk) {
        this.sub_type_id_pk = sub_type_id_pk;
    }

    public String getSub_type_name() {
        return sub_type_name;
    }

    public void setSub_type_name(String sub_type_name) {
        this.sub_type_name = sub_type_name;
    }

}