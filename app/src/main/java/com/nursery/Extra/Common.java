package com.nursery.Extra;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;


import com.nursery.Activity.MainPage;
import com.nursery.Model.AllList;
import com.nursery.Model.CartResponse;
import com.nursery.Model.ProfileResponse;
import com.nursery.Retrofit.Api;
import com.nursery.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prasad
 */

public class Common {

    public static final String SHARED_PREF = "userData";
    public static List<CartResponse> cartResponseList = new ArrayList<>();
    public static List<ProfileResponse> profileResponseList = new ArrayList<>();

    public static void saveUserData(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSavedUserData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        return pref.getString(key, "");

    }

    public static void getCartCount(final Context context, final String CustomerId){

        MainPage.progressBar.setVisibility(View.VISIBLE);
        MainPage.cartCount.setVisibility(View.GONE);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getCartCount(MainPage.userId, CustomerId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                cartResponseList = allList.getCartResponseList();

                if (cartResponseList.size()==0){
                    MainPage.progressBar.setVisibility(View.GONE);
                    MainPage.cartCount.setVisibility(View.GONE);
                } else {

                    for (int i=0;i<cartResponseList.size();i++){

                        MainPage.cartCount.setText(cartResponseList.get(i).getQuantity());
                        MainPage.subAmount = cartResponseList.get(i).getSubAmount();

                        MainPage.progressBar.setVisibility(View.GONE);
                        MainPage.cartCount.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                MainPage.progressBar.setVisibility(View.GONE);
                MainPage.cartCount.setVisibility(View.GONE);
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    public static void getProfile(final Context context){

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getProfile(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                profileResponseList = allList.getProfileResponseList();

                if (profileResponseList.size()==0){

                } else {

                    for (int i=0;i<profileResponseList.size();i++){

                        MainPage.name = profileResponseList.get(i).getName();
                        MainPage.mobileNumber = profileResponseList.get(i).getMobileNumber();
                        MainPage.securityPin = profileResponseList.get(i).getSecurityPin();
                        MainPage.shopStatus =  profileResponseList.get(i).getShopStatus();
                        Log.e("securityPin", ""+MainPage.securityPin);
                        Common.saveUserData(context,"securityPin", profileResponseList.get(i).getSecurityPin());
                        Common.saveUserData(context,"shopStatus", profileResponseList.get(i).getShopStatus());

                    }

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", "Profile"+t.getMessage());
            }
        });

    }

}
