package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;
import com.sawant_nursery.Fragment.LedgerResponse;

import java.util.List;


public class AllList {

    @SerializedName("categoryResponse")
    List<CategoryResponse> categoryResponseList;

    @SerializedName("subCategoryResponse")
    List<CategoryResponse> subcategoryResponseList;

    @SerializedName("customerResponse")
    List<CustomerResponse> customerResponseList;

    @SerializedName("productResponse")
    List<ProductResponse> productResponseList;

    @SerializedName("sizeResponse")
    List<SizeResponse> sizeResponseList;

    @SerializedName("bagSizeResponse")
    List<BagSizeResponse> bagSizeResponseList;

    @SerializedName("cartResponse")
    List<CartResponse> cartResponseList;

    @SerializedName("profileResponse")
    List<ProfileResponse> profileResponseList;

    @SerializedName("invoiceResponse")
    List<InvoiceResponse> invoiceResponseList;

    @SerializedName("invoiceResponse")
    List<LedgerResponse> ledgerResponseList;



    //All Getter and Setter.....
    public List<CategoryResponse> getCategoryResponseList() {
        return categoryResponseList;
    }

    public void setCategoryResponseList(List<CategoryResponse> categoryResponseList) {
        this.categoryResponseList = categoryResponseList;
    }

    public List<CustomerResponse> getCustomerResponseList() {
        return customerResponseList;
    }

    public void setCustomerResponseList(List<CustomerResponse> customerResponseList) {
        this.customerResponseList = customerResponseList;
    }

    public List<ProductResponse> getProductResponseList() {
        return productResponseList;
    }

    public void setProductResponseList(List<ProductResponse> productResponseList) {
        this.productResponseList = productResponseList;
    }

    public List<SizeResponse> getSizeResponseList() {
        return sizeResponseList;
    }

    public void setSizeResponseList(List<SizeResponse> sizeResponseList) {
        this.sizeResponseList = sizeResponseList;
    }

    public List<CartResponse> getCartResponseList() {
        return cartResponseList;
    }

    public void setCartResponseList(List<CartResponse> cartResponseList) {
        this.cartResponseList = cartResponseList;
    }

    public List<BagSizeResponse> getBagSizeResponseList() {
        return bagSizeResponseList;
    }

    public void setBagSizeResponseList(List<BagSizeResponse> bagSizeResponseList) {
        this.bagSizeResponseList = bagSizeResponseList;
    }

    public List<ProfileResponse> getProfileResponseList() {
        return profileResponseList;
    }

    public void setProfileResponseList(List<ProfileResponse> profileResponseList) {
        this.profileResponseList = profileResponseList;
    }

    public List<InvoiceResponse> getInvoiceResponseList() {
        return invoiceResponseList;
    }

    public void setInvoiceResponseList(List<InvoiceResponse> invoiceResponseList) {
        this.invoiceResponseList = invoiceResponseList;
    }

    public List<CategoryResponse> getSubcategoryResponseList() {
        return subcategoryResponseList;
    }

    public void setSubcategoryResponseList(List<CategoryResponse> subcategoryResponseList) {
        this.subcategoryResponseList = subcategoryResponseList;
    }

    public List<LedgerResponse> getLedgerResponseList() {
        return ledgerResponseList;
    }

    public void setLedgerResponseList(List<LedgerResponse> ledgerResponseList) {
        this.ledgerResponseList = ledgerResponseList;
    }
}
