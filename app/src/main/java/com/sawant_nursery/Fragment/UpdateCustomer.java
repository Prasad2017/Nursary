package com.sawant_nursery.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateCustomer extends Fragment {

    View view;
    @BindViews({R.id.businessName, R.id.officeNumber, R.id.contactPerson, R.id.contactPersonNumber, R.id.email, R.id.businessAddress, R.id.city, R.id.state, R.id.pincode})
    List<FormEditText> formEditTexts;
    String type, customerId;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_customer, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Add Customer");
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (MainPage.shopStatus.equals("Retailer_on")){
            type = "Retailer_on";
        } else if (MainPage.shopStatus.equals("Wholesaler_on")){
            type = "Wholesaler_on";
        } else if (MainPage.shopStatus.equals("Retailer_off") && MainPage.shopStatus.equals("Wholesaler_off")){
            Toast.makeText(getActivity(), "Please Select Shop Status in setting", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Please Select Shop Status in setting", Toast.LENGTH_SHORT).show();
        }

        Bundle bundle = getArguments();
        customerId = bundle.getString("customerId");
        formEditTexts.get(0).setText(bundle.getString("customerName"));
        formEditTexts.get(1).setText(bundle.getString("customerNumber"));
        formEditTexts.get(2).setText(bundle.getString("contactPerson"));
        formEditTexts.get(3).setText(bundle.getString("contactPersonNumber"));
        formEditTexts.get(4).setText(bundle.getString("email"));
        formEditTexts.get(5).setText(bundle.getString("address"));
        formEditTexts.get(6).setText(bundle.getString("city"));
        formEditTexts.get(7).setText(bundle.getString("state"));
        formEditTexts.get(8).setText(bundle.getString("pincode"));

        return view;


    }

    @OnClick({R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:


                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity()
                        && formEditTexts.get(3).testValidity() && formEditTexts.get(4).testValidity() && formEditTexts.get(5).testValidity()
                        && formEditTexts.get(6).testValidity() && formEditTexts.get(7).testValidity() && formEditTexts.get(8).testValidity()){

                    AddCustomer(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString(), formEditTexts.get(2).getText().toString(),
                            formEditTexts.get(3).getText().toString(), formEditTexts.get(4).getText().toString(), formEditTexts.get(5).getText().toString(),
                            formEditTexts.get(6).getText().toString(), formEditTexts.get(7).getText().toString(), formEditTexts.get(8).getText().toString());

                }

                break;
        }

    }

    private void AddCustomer(String businessName, String officeNumber, String contactPerson, String PersonNumber, String email, String businessAddress, String city, String state, String pincode) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Account is in Updating");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.updateCustomer(customerId, type, businessName, officeNumber, contactPerson, PersonNumber, email, businessAddress, city, state, pincode);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if (response.body().getSuccess().equals("true")){
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainPage)getActivity()).removeCurrentFragmentAndMoveBack();
                    ((MainPage)getActivity()).loadFragment(new CustomerList(), true);
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", ""+t.getMessage());
            }
        });

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

        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
