package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {

    @SerializedName("amount_id_pk")
    String amountId;

    @SerializedName("product_type_id")
    String categoryId;

    @SerializedName("product_type")
    String categoryType;

    @SerializedName("master_superadmin_product_id_pk")
    String productId;

    @SerializedName("master_superadmin_product_name")
    String productName;

    @SerializedName("master_superadmin_product_image")
    String categoryImage;

    @SerializedName("master_superadmin_product_retail_price")
    String retailPrice;

    @SerializedName("master_superadmin_product_wholesale_price")
    String wholesalerPrice;

    @SerializedName("master_superadmin_product_size")
    String productSize;

    @SerializedName("master_superadmin_bag_size")
    String bagSize;

    @SerializedName("cgst")
    String cgst;

    @SerializedName("sgst")
    String sgst;

    @SerializedName("igst")
    String igst;

    @SerializedName("plantSizeName")
    String plantSizeName;

    @SerializedName("bagSizeName")
    String bagSizeName;

    @SerializedName("taxType")
    String taxType;

    private boolean isSelected;



    //All getter and Setter...
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(String wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
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

    public String getBagSize() {
        return bagSize;
    }

    public void setBagSize(String bagSize) {
        this.bagSize = bagSize;
    }

    public String getAmountId() {
        return amountId;
    }

    public void setAmountId(String amountId) {
        this.amountId = amountId;
    }

    public String getPlantSizeName() {
        return plantSizeName;
    }

    public void setPlantSizeName(String plantSizeName) {
        this.plantSizeName = plantSizeName;
    }

    public String getBagSizeName() {
        return bagSizeName;
    }

    public void setBagSizeName(String bagSizeName) {
        this.bagSizeName = bagSizeName;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }


}
