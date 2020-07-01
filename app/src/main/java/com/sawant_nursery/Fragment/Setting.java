package com.sawant_nursery.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.sawant_nursery.Activity.Login;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Database.NurseryDatabase;
import com.sawant_nursery.Extra.Common;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.Model.ProfileResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Setting extends Fragment {

    View view;
    @BindViews({R.id.retailerSwitch, R.id.wholesalerSwitch})
    List<LabeledSwitch> labeledSwitches;
    String shopStatus;
    @BindView(R.id.setPin)
            TextView setPin;
    NurseryDatabase nurseryDatabase;
    List<ProfileResponse> profileResponseList = new ArrayList<>();



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("");
        nurseryDatabase = new NurseryDatabase(getActivity());


        if (MainPage.shopStatus.equals("Retailer_on")){
            labeledSwitches.get(0).setOn(true);
            labeledSwitches.get(1).setOn(false);
        } else if (MainPage.shopStatus.equals("Wholesaler_on")){
            labeledSwitches.get(1).setOn(true);
            labeledSwitches.get(0).setOn(false);
        } else {
            labeledSwitches.get(0).setOn(false);
            labeledSwitches.get(1).setOn(false);
        }

        if (!MainPage.securityPin.equals("0")){
            setPin.setText("Change Pin");
        } else {
            setPin.setText("Set Pin");
        }


        if (!MainPage.securityPin.equals("0")) {

            labeledSwitches.get(0).setOnToggledListener(new OnToggledListener() {
                @Override
                public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                    if (isOn == true) {
                        shopStatus = "Retailer_on";
                        labeledSwitches.get(0).setOn(true);
                        labeledSwitches.get(1).setOn(false);
                        addStatus(shopStatus);
                    } else {
                        shopStatus = "Wholesaler_on";
                        labeledSwitches.get(1).setOn(true);
                        labeledSwitches.get(0).setOn(false);
                        addStatus(shopStatus);
                    }

                }
            });


        } else {
            Toast.makeText(getActivity(), "Please set security code", Toast.LENGTH_SHORT).show();
        }


        if (!MainPage.securityPin.equals("0")) {

            labeledSwitches.get(1).setOnToggledListener(new OnToggledListener() {
                @Override
                public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                    if (isOn==true){
                        shopStatus = "Wholesaler_on";
                        labeledSwitches.get(1).setOn(true);
                        labeledSwitches.get(0).setOn(false);
                        addStatus(shopStatus);
                    } else {
                        shopStatus = "Retailer_on";
                        labeledSwitches.get(0).setOn(true);
                        labeledSwitches.get(1).setOn(false);
                        addStatus(shopStatus);
                    }

                }
            });

        } else {
            Toast.makeText(getActivity(), "Please set security code", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void addStatus(String shopStatus) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Status in verification");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.setshopStatus(MainPage.userId, shopStatus);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().equals("true")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Common.getProfile(getActivity());
                } else if (response.body().getSuccess().equals("false")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("PinError", ""+t.getMessage());
            }
        });

    }

    @OnClick({R.id.pinLayout, R.id.aboutLayout, R.id.logoutLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pinLayout:
                ((MainPage)getActivity()).loadFragment(new ChangePin(), true);
                break;

            case R.id.aboutLayout:
                ((MainPage)getActivity()).loadFragment(new AppInfo(), true);
                break;

            case R.id.logoutLayout:
                logout();
                break;

        }
    }

    private void logout() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView txtyes = dialog.findViewById(R.id.yes);
        TextView txtno = dialog.findViewById(R.id.no);

        txtno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Common.saveUserData(getActivity(), "accessToken", "");
                File file1 = new File("data/data/com.sawant_nursery/shared_prefs/user.xml");
                if (file1.exists()) {
                    file1.delete();
                }

                Intent intent = new Intent(getActivity(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        dialog.show();

    }

    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainPage.title.setVisibility(View.GONE);
        MainPage.titleLayout.setVisibility(View.VISIBLE);
        MainPage.importProduct.setVisibility(View.GONE);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.saveProduct.setVisibility(View.GONE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            Common.getProfile(getActivity());
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
