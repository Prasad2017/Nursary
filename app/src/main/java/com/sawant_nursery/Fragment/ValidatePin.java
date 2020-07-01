package com.sawant_nursery.Fragment;

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
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.Common;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ValidatePin extends Fragment {


    View view;
    @BindView(R.id.otpView)
    OtpView otpView;
    String securityCode;
    @BindView(R.id.proceed)
    TextView proceed;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_validate_pin, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("");

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                try {
                    securityCode = otp;
                    Log.e("securityCode", ""+securityCode);
                    Log.e("securityPin", ""+MainPage.securityPin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        otpView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (!MainPage.securityPin.equals("0")){

                        if (MainPage.securityPin.equals(securityCode)){
                            ((MainPage)getActivity()).removeCurrentFragmentAndMoveBack();
                            ((MainPage)getActivity()).loadFragment(new Setting(), true);
                        } else {
                            Toast.makeText(getActivity(), "Please enter valid Pin", Toast.LENGTH_SHORT).show();
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
                    if (securityCode.equals(MainPage.securityPin)){
                        ((MainPage)getActivity()).removeCurrentFragmentAndMoveBack();
                        ((MainPage)getActivity()).loadFragment(new Setting(), true);
                    } else {
                        Toast.makeText(getActivity(), "Please enter valid Pin", Toast.LENGTH_SHORT).show();
                        otpView.setError("Please enter valid Pin");
                    }
                }
            }
        });

        return view;

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