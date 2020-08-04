package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;

public class StateResponse {

    @SerializedName("state_id")
    public String state_id;

    @SerializedName("state_name")
    public String state_name;


    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}
