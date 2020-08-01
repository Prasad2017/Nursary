package com.sawant_nursery.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.Common;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddSecurityPin extends Fragment {


    View view;
    @BindView(R.id.otpView)
    OtpView otpView;
    @BindView(R.id.confirmOtpView)
    OtpView confirmOtpView;
    String securityCode, confirmCode;
    @BindView(R.id.proceed)
    TextView proceed;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_security_pin, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Set Pin");
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                try {
                    securityCode = otp;
                    int length = securityCode.trim().length();
                    if (securityCode.length()>5){
                        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        confirmOtpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                try {
                    confirmCode = otp;
                    int length = confirmCode.trim().length();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        confirmOtpView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (confirmCode.equals(securityCode)) {
                        setOTP(securityCode);
                    } else {
                        Toast.makeText(getActivity(), "Please enter confirm code", Toast.LENGTH_SHORT).show();
                        confirmOtpView.setError("Please enter confirm code");
                    }

                    return true;
                }
                return false;
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmCode.equals(securityCode)) {
                    setOTP(securityCode);
                } else {
                    Toast.makeText(getActivity(), "Please enter confirm code", Toast.LENGTH_SHORT).show();
                    confirmOtpView.setError("Please enter confirm code");
                }
            }
        });

        return view;

    }

    private void setOTP(String securityCode) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("PIN in verification");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.setPin(MainPage.userId, securityCode);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().equals("true")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Pin has been set.", Toast.LENGTH_SHORT).show();
                    Common.getProfile(getActivity());
                    ((MainPage)getActivity()).removeCurrentFragmentAndMoveBack();
                    ((MainPage)getActivity()).loadFragment(new Setting(), true);
                } else if (response.body().getSuccess().equals("false")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Pin has been not set.", Toast.LENGTH_SHORT).show();
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