package com.sawant_nursery.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.CartResponse;
import com.sawant_nursery.Model.CustomerResponse;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillingDetails extends Fragment {


    View view;
    @BindViews({R.id.invoice, R.id.contactPerson, R.id.personNumber, R.id.gst, R.id.poNumber, R.id.billingAddress, R.id.deliveryAddress, R.id.vehicleNumber})
    List<FormEditText> invoiceFormEditTexts;
    @BindViews({R.id.subAmount, R.id.discount, R.id.otherCharges, R.id.transport, R.id.grandAmount, R.id.pendingAmount, R.id.amountPayable})
    List<FormEditText> paymentFormEditTexts;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindViews({R.id.invoiceLayout, R.id.paymentLayout})
    List<LinearLayout> linearLayouts;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    String customerType, customerId, customerName, pendingAmount;
    float grandAmount;
    List<PendingResponse> pendingResponseList = new ArrayList<>();
    List<CustomerResponse> customerResponseList = new ArrayList<>();



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_billing_details, container, false);
        ButterKnife.bind(this, view);
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Bundle bundle = getArguments();
        customerType = bundle.getString("customerType");
        customerId = bundle.getString("customerId");
        customerName = bundle.getString("customerName");

        Log.e("customerId", ""+customerId);

        try{

            paymentFormEditTexts.get(0).setText(""+CustomerCartList.adapter.totalAmountPayable);
            paymentFormEditTexts.get(4).setText(""+CustomerCartList.adapter.totalAmountPayable);

        }catch (Exception e){
            e.printStackTrace();
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked){
                    invoiceFormEditTexts.get(6).setText("");
                    invoiceFormEditTexts.get(6).setSelection(invoiceFormEditTexts.get(6).getText().toString().length());
                } else {
                    invoiceFormEditTexts.get(6).setText(invoiceFormEditTexts.get(5).getText().toString());
                    invoiceFormEditTexts.get(6).setSelection(invoiceFormEditTexts.get(6).getText().toString().length());
                }
            }
        });

        paymentFormEditTexts.get(1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                try{

                    if (paymentFormEditTexts.get(0).testValidity() && paymentFormEditTexts.get(1).testValidity() &&
                            paymentFormEditTexts.get(2).testValidity() && paymentFormEditTexts.get(3).testValidity() && paymentFormEditTexts.get(5).testValidity()) {

                        if (paymentFormEditTexts.get(1).getText().toString().length()>0) {
                            if (Float.parseFloat(paymentFormEditTexts.get(1).getText().toString()) < 0f) {
                                paymentFormEditTexts.get(1).setText("");
                                paymentFormEditTexts.get(1).setSelection(paymentFormEditTexts.get(1).getText().toString().length());
                            } else if (Float.parseFloat(paymentFormEditTexts.get(1).getText().toString()) > 100f) {
                                paymentFormEditTexts.get(1).setText("100");
                                paymentFormEditTexts.get(1).setSelection(paymentFormEditTexts.get(1).getText().toString().length());
                            } else {

                                float subAmount = Float.parseFloat(paymentFormEditTexts.get(0).getText().toString());
                                float discount = Float.parseFloat(paymentFormEditTexts.get(1).getText().toString());
                                if (discount>0f) {
                                    discount = (discount * subAmount) / 100;
                                }
                                float otherCharges = Float.parseFloat(paymentFormEditTexts.get(2).getText().toString());
                                float transport = Float.parseFloat(paymentFormEditTexts.get(3).getText().toString());
                                float pending = Float.parseFloat(paymentFormEditTexts.get(5).getText().toString());
                                grandAmount = subAmount + otherCharges + transport - discount;
                                float grandTotal = subAmount + otherCharges + transport - discount + pending;

                                paymentFormEditTexts.get(4).setText("" + String.format(Locale.US, "%.2f", grandTotal));
                            }
                        } else {
                            paymentFormEditTexts.get(1).setText("");
                            paymentFormEditTexts.get(1).setSelection(paymentFormEditTexts.get(1).getText().toString().length());
                        }


                    } else {

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        paymentFormEditTexts.get(2).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                try {

                    if (paymentFormEditTexts.get(0).testValidity() && paymentFormEditTexts.get(1).testValidity() &&
                            paymentFormEditTexts.get(2).testValidity() && paymentFormEditTexts.get(3).testValidity() && paymentFormEditTexts.get(5).testValidity()) {

                        if (paymentFormEditTexts.get(2).getText().toString().length() < 0) {
                            paymentFormEditTexts.get(2).setText("");
                            paymentFormEditTexts.get(2).setSelection(paymentFormEditTexts.get(2).getText().toString().length());
                        } else {

                            float subAmount = Float.parseFloat(paymentFormEditTexts.get(0).getText().toString());
                            float discount = Float.parseFloat(paymentFormEditTexts.get(1).getText().toString());
                            if (discount>0f) {
                                discount = (discount * subAmount) / 100;
                            }
                            float otherCharges = Float.parseFloat(paymentFormEditTexts.get(2).getText().toString());
                            float transport = Float.parseFloat(paymentFormEditTexts.get(3).getText().toString());
                            float pending = Float.parseFloat(paymentFormEditTexts.get(5).getText().toString());
                            float grandTotal = subAmount + otherCharges + transport - discount + pending;
                            grandAmount = subAmount + otherCharges + transport - discount;
                            paymentFormEditTexts.get(4).setText("" + String.format(Locale.US, "%.2f", grandTotal));
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        paymentFormEditTexts.get(3).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                try {

                    if (paymentFormEditTexts.get(0).testValidity() && paymentFormEditTexts.get(1).testValidity() &&
                            paymentFormEditTexts.get(2).testValidity() && paymentFormEditTexts.get(3).testValidity() && paymentFormEditTexts.get(5).testValidity()) {

                        if (paymentFormEditTexts.get(3).getText().toString().length() < 0) {
                            paymentFormEditTexts.get(3).setText("");
                        } else {

                            float subAmount = Float.parseFloat(paymentFormEditTexts.get(0).getText().toString());
                            float discount = Float.parseFloat(paymentFormEditTexts.get(1).getText().toString());
                            if (discount>0f) {
                                discount = (discount * subAmount) / 100;
                            }
                            float otherCharges = Float.parseFloat(paymentFormEditTexts.get(2).getText().toString());
                            float transport = Float.parseFloat(paymentFormEditTexts.get(3).getText().toString());
                            float pending = Float.parseFloat(paymentFormEditTexts.get(5).getText().toString());
                            float grandTotal = subAmount + otherCharges + transport - discount + pending;
                            grandAmount = subAmount + otherCharges + transport - discount;
                            paymentFormEditTexts.get(4).setText("" + String.format(Locale.US, "%.2f", grandTotal));
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

        paymentFormEditTexts.get(6).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String s1 = s.toString();
                try {

                    if (paymentFormEditTexts.get(0).testValidity() && paymentFormEditTexts.get(1).testValidity() &&
                            paymentFormEditTexts.get(2).testValidity() && paymentFormEditTexts.get(3).testValidity() &&
                            paymentFormEditTexts.get(5).testValidity() && paymentFormEditTexts.get(6).testValidity()) {

                        if (paymentFormEditTexts.get(6).getText().toString().length() < 0) {
                            paymentFormEditTexts.get(5).setText(pendingAmount);
                        } else {

                            float subAmount = Float.parseFloat(paymentFormEditTexts.get(4).getText().toString());
                            float payableAmount = Float.parseFloat(paymentFormEditTexts.get(6).getText().toString());
                            float pending = subAmount - payableAmount;

                            paymentFormEditTexts.get(5).setText("" + String.format(Locale.US, "%.2f", pending));
                        }

                    } else {
                        paymentFormEditTexts.get(5).setText(pendingAmount);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    @OnClick({R.id.next, R.id.previous, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:

                if (invoiceFormEditTexts.get(0).testValidity() && invoiceFormEditTexts.get(1).testValidity() && invoiceFormEditTexts.get(2).testValidity()
                        && invoiceFormEditTexts.get(3).testValidity() && invoiceFormEditTexts.get(4).testValidity() && invoiceFormEditTexts.get(5).testValidity()
                        && invoiceFormEditTexts.get(6).testValidity() && invoiceFormEditTexts.get(7).testValidity()) {

                    linearLayouts.get(0).setVisibility(View.GONE);
                    linearLayouts.get(1).setVisibility(View.VISIBLE);

                }

                break;

            case R.id.previous:
                linearLayouts.get(1).setVisibility(View.GONE);
                linearLayouts.get(0).setVisibility(View.VISIBLE);
                break;

            case R.id.save:

                if (invoiceFormEditTexts.get(0).testValidity() && invoiceFormEditTexts.get(1).testValidity() && invoiceFormEditTexts.get(2).testValidity()
                        && invoiceFormEditTexts.get(3).testValidity() && invoiceFormEditTexts.get(4).testValidity() && invoiceFormEditTexts.get(5).testValidity()
                        && invoiceFormEditTexts.get(6).testValidity() && invoiceFormEditTexts.get(7).testValidity()){

                    if (paymentFormEditTexts.get(0).testValidity() && paymentFormEditTexts.get(4).testValidity() && paymentFormEditTexts.get(5).testValidity() && paymentFormEditTexts.get(6).testValidity()) {

                        addFinalOrder(invoiceFormEditTexts.get(0).getText().toString(), invoiceFormEditTexts.get(1).getText().toString(), invoiceFormEditTexts.get(2).getText().toString(),
                                invoiceFormEditTexts.get(3).getText().toString(), invoiceFormEditTexts.get(4).getText().toString(), invoiceFormEditTexts.get(5).getText().toString(),
                                invoiceFormEditTexts.get(6).getText().toString(), invoiceFormEditTexts.get(7).getText().toString(), paymentFormEditTexts.get(0).getText().toString(),
                                paymentFormEditTexts.get(1).getText().toString(), paymentFormEditTexts.get(2).getText().toString(), paymentFormEditTexts.get(3).getText().toString(),
                                paymentFormEditTexts.get(4).getText().toString(), paymentFormEditTexts.get(5).getText().toString(), paymentFormEditTexts.get(6).getText().toString(), ""+grandAmount);

                    } else {
                        linearLayouts.get(1).setVisibility(View.VISIBLE);
                        linearLayouts.get(0).setVisibility(View.GONE);
                    }

                } else {
                    linearLayouts.get(0).setVisibility(View.VISIBLE);
                    linearLayouts.get(1).setVisibility(View.GONE);
                }

                break;
        }

    }

    private void addFinalOrder(String invoiceTo, String contactPerson, String personNumber, String gstNo, String poNumber, String billingAddress, String deliveryAddress, String vehicleNumber, String subAmount, String discount, String otherCharges, String transport, String grandTotal, String pendingAmount, String payableAmount, String grandAmount) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Order is creating");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.addFinalOrders(MainPage.userId, customerId, customerName, invoiceTo, contactPerson, personNumber, gstNo, poNumber, billingAddress, deliveryAddress, vehicleNumber, subAmount, discount, otherCharges, transport, grandTotal, pendingAmount, payableAmount, grandAmount);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().equals("true")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainPage) getActivity()).loadFragment(new Dashboard(), false);
                } else if (response.body().getSuccess().equals("false")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
        MainPage.saveProduct.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getPendingAmount();
            getCustomerDetails();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCustomerDetails() {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getCustomerDetails(MainPage.userId, customerId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                customerResponseList = allList.getCustomerResponseLists();

                if (customerResponseList.size()==0){

                } else {

                    for (int i=0;i<customerResponseList.size();i++){

                        invoiceFormEditTexts.get(0).setText(customerResponseList.get(i).getCustomerName());
                        invoiceFormEditTexts.get(1).setText(customerResponseList.get(i).getContactPerson());
                        invoiceFormEditTexts.get(2).setText(customerResponseList.get(i).getContact_person_number());
                        invoiceFormEditTexts.get(3).setText(customerResponseList.get(i).getGst());
                        invoiceFormEditTexts.get(5).setText(customerResponseList.get(i).getAddress());

                    }

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                paymentFormEditTexts.get(0).setText("0");
                paymentFormEditTexts.get(4).setText("0");
                Log.e("Error", ""+t.getMessage());
            }
        });


    }

    private void getPendingAmount() {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getProductAmount(MainPage.userId, customerId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                pendingResponseList = allList.getPendingResponseList();

                if (pendingResponseList.size()==0){
                    paymentFormEditTexts.get(5).setText("0");
                } else {

                    for (int i=0;i<pendingResponseList.size();i++){

                        pendingAmount = pendingResponseList.get(i).getPending_amount();

                        if (pendingAmount!=null){
                            pendingAmount = pendingAmount;
                            paymentFormEditTexts.get(5).setText(pendingResponseList.get(i).getPending_amount());
                        } else {
                            pendingAmount="0";
                            paymentFormEditTexts.get(5).setText("0");
                        }

                        try {
                            float subAmount = Float.parseFloat(paymentFormEditTexts.get(0).getText().toString());
                            float discount = Float.parseFloat(paymentFormEditTexts.get(1).getText().toString());
                            if (discount > 0f) {
                                discount = (discount * subAmount) / 100;
                            }
                            float otherCharges = Float.parseFloat(paymentFormEditTexts.get(2).getText().toString());
                            float transport = Float.parseFloat(paymentFormEditTexts.get(3).getText().toString());
                            float pending = Float.parseFloat(paymentFormEditTexts.get(5).getText().toString());
                            float grandTotal = subAmount + otherCharges + transport - discount + pending;
                            grandAmount=subAmount + otherCharges + transport - discount;
                            paymentFormEditTexts.get(4).setText("" + String.format(Locale.US, "%.2f", grandTotal));

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                paymentFormEditTexts.get(5).setText("0");
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

}
