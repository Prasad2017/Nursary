package com.sawant_nursery.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sawant_nursery.Activity.Login;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Adapter.LedgerAdapter;
import com.sawant_nursery.Extra.Common;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.LedgerResponse;
import com.sawant_nursery.Model.LoginResponse;
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


public class LedgerList extends Fragment {

    View view;
    @BindView(R.id.customerRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noCategorytxt)
    TextView noCategorytxt;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.ledgerLayout)
    LinearLayout ledgerLayout;
    List<LedgerResponse> ledgerResponseList = new ArrayList<>();
    List<LedgerResponse> searchCustomerResponseList = new ArrayList<>();
    LedgerAdapter adapter;
    String customerId;
    @BindViews({R.id.receivedAmount, R.id.balanceAmount, R.id.totalAmount})
    List<TextView> textViews;
    double receivedAmount=0f, balanceAmount=0f, totalAmount=0f;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ledger, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("");

        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Bundle bundle = getArguments();
        customerId = bundle.getString("customerId");


        return view;

    }

    @OnClick({R.id.paymentIn, R.id.paymentOut})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.paymentIn:

                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.setTitle("Payment is in process");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
                Call<LoginResponse> call = apiInterface.paymentIn(customerId, MainPage.userId, ""+balanceAmount);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.body().getSuccess().equalsIgnoreCase("true")){
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            ((MainPage) getActivity()).removeCurrentFragmentAndMoveBack();
                            LedgerList ledgerList = new LedgerList();
                            Bundle bundle = new Bundle();
                            bundle.putString("customerId", customerId);
                            ledgerList.setArguments(bundle);
                            ((MainPage) getActivity()).loadFragment(ledgerList, true);
                        } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("paymentIn", ""+t.getMessage());
                    }
                });

                break;

            case R.id.paymentOut:

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.payment_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                TextView txtyes = dialog.findViewById(R.id.yes);
                EditText purchaseAmount = dialog.findViewById(R.id.purchaseAmount);
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

                        ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Loading...");
                        progressDialog.setTitle("Payment is in process");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
                        Call<LoginResponse> call = apiInterface.paymentOut(customerId, MainPage.userId, ""+purchaseAmount.getText().toString().trim());
                        call.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                                if (response.body().getSuccess().equalsIgnoreCase("true")){
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    ((MainPage) getActivity()).removeCurrentFragmentAndMoveBack();
                                    LedgerList ledgerList = new LedgerList();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("customerId", customerId);
                                    ledgerList.setArguments(bundle);
                                    ((MainPage) getActivity()).loadFragment(ledgerList, true);
                                } else if (response.body().getSuccess().equalsIgnoreCase("false")){
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("paymentOut", ""+t.getMessage());
                            }
                        });

                    }
                });

                dialog.show();

                break;

        }
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
            getInvoiceList();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getInvoiceList() {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getLedgerList(MainPage.userId, customerId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                ledgerResponseList = allList.getLedgerResponseList();

                if (ledgerResponseList.size() == 0){
                    noCategorytxt.setVisibility(View.VISIBLE);
                    ledgerLayout.setVisibility(View.GONE);
                }else {

                    receivedAmount = Double.parseDouble(ledgerResponseList.get(0).getCustomer_paid_amount());
                    balanceAmount = Double.parseDouble(ledgerResponseList.get(0).getCustomer_pending_amount());
                    totalAmount = Double.parseDouble(ledgerResponseList.get(0).getCustomer_order_amount());

                    textViews.get(0).setText("Received Amount: "+receivedAmount);
                    textViews.get(1).setText("Balance Amount: "+balanceAmount);
                    textViews.get(2).setText("Total Amount: "+totalAmount);

                    adapter = new LedgerAdapter(getActivity(), ledgerResponseList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(ledgerResponseList.size() - 1);

                    noCategorytxt.setVisibility(View.GONE);
                    ledgerLayout.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("CustomerError", ""+t.getMessage());
                noCategorytxt.setVisibility(View.VISIBLE);
                ledgerLayout.setVisibility(View.GONE);
            }
        });

    }

}