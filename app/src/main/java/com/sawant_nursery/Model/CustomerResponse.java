package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;

public class CustomerResponse {

    @SerializedName("c_id")
    String customerId;

    @SerializedName("business_name")
    String customerName;

    @SerializedName("office_contact_number")
    String mobileNumber;

    @SerializedName("contact_person")
    String contactPerson;

    @SerializedName("contact_person_number")
    String contact_person_number;

    @SerializedName("email")
    String emailId;

    @SerializedName("business_address")
    String address;

    @SerializedName("city")
    String city;

    @SerializedName("taluka")
    String taluka;

    @SerializedName("state")
    String state;

    @SerializedName("stateId")
    String stateId;

    @SerializedName("pincode")
    String pincode;

    @SerializedName("reg_date")
    String reg_date;

    @SerializedName("type")
    String type;

    @SerializedName("gst")
    String gst;


    //To Getter ans Setter Method
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContact_person_number() {
        return contact_person_number;
    }

    public void setContact_person_number(String contact_person_number) {
        this.contact_person_number = contact_person_number;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}
