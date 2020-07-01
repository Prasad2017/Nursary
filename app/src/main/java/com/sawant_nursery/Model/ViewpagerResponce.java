package com.sawant_nursery.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ViewpagerResponce {


    @SerializedName("sliderResponse")
    private List<Viewpager> viewpagerList;

    public List<Viewpager> getViewpagerList() {
        return viewpagerList;
    }

    public void setViewpagerList(List<Viewpager> viewpagerList) {
        this.viewpagerList = viewpagerList;
    }

}

