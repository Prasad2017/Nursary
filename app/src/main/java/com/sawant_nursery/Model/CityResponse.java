package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;

public class CityResponse {

    @SerializedName("city_id")
    public String city_id;

    @SerializedName("city_name")
    public String city_name;


    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
