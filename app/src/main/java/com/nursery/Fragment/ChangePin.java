package com.nursery.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.nursery.Activity.MainPage;
import com.nursery.Extra.Common;
import com.nursery.Extra.DetectConnection;
import com.nursery.Model.LoginResponse;
import com.nursery.R;
import com.nursery.Retrofit.Api;
import com.nursery.Retrofit.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePin extends Fragment {

    View view;
    @BindViews({R.id.oldOtpView, R.id.otpView})
    List<OtpView> otpViews;
    String securityCode, changeSecurityCode;
    @BindView(R.id.proceed)
    TextView proceed;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chnage_pin, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Changeq Pin");

        otpViews.get(0).setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                try {
                    securityCode = otp;
                    int length = securityCode.trim().length();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        otpViews.get(1).setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                try {
                    changeSecurityCode = otp;
                    int length = changeSecurityCode.trim().length();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        otpViews.get(1).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (!MainPage.securityPin.equals("0")){
                        if (MainPage.securityPin.equals(securityCode)){
                            setNewPin(changeSecurityCode);
                        } else {
                            Toast.makeText(getActivity(), "Please enter valid Pin", Toast.LENGTH_SHORT).show();
                            otpViews.get(0).setError("Please enter valid old Pin");
                        }
                    }

                    return true;
                }
                return false;
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!MainPage.securityPin.equals("0")){
                    if (MainPage.securityPin.equals(securityCode)){
                        setNewPin(changeSecurityCode);
                    } else {
                        Toast.makeText(getActivity(), "Please enter old valid Pin", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return view;

    }

    private void setNewPin(String changeSecurityCode) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("PIN in verification");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.setPin(MainPage.userId, changeSecurityCode);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().equals("true")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Pin has been changed.", Toast.LENGTH_SHORT).show();
                    Common.getProfile(getActivity());
                    ((MainPage)getActivity()).removeCurrentFragmentAndMoveBack();
                    ((MainPage)getActivity()).loadFragment(new Setting(), true);
                } else if (response.body().getSuccess().equals("false")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Pin has been not changed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("PinError", ""+t.getMessage());
            }
        });


    }


    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainPage.titleLayout.setVisibility(View.GONE);
        MainPage.importProduct.setVisibility(View.GONE);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.saveProduct.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            Common.getProfile(getActivity());
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}