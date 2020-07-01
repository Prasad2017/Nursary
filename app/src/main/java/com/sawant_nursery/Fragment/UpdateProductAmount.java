package com.sawant_nursery.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.BagSizeResponse;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.Model.ProductResponse;
import com.sawant_nursery.Model.SizeResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductAmount extends Fragment {

    View view;
    @BindViews({R.id.plantSize, R.id.bagSize})
    List<SearchableSpinner> searchableSpinners;
    @BindViews({R.id.retailerAmount, R.id.wholesalerAmount, R.id.productName})
    List<FormEditText> formEditTexts;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    String productId, productName, plantSizeId, plantSizeName, bagSizeId, bagSizeName;
    String[] plantSizeIdList, plantSizeNameList, bagSizeIdList, bagSizeNameList;
    List<SizeResponse> sizeResponseList = new ArrayList<>();
    List<BagSizeResponse> bagSizeResponseList = new ArrayList<>();
    List<ProductResponse> amountProductResponseList = new ArrayList<>();



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_product_amount, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("");


        Bundle bundle = getArguments();
        productId = bundle.getString("productId");
        productName = bundle.getString("productName");
        plantSizeId = bundle.getString("plantSizeId");
        plantSizeName = bundle.getString("plantSizeName");
        bagSizeId = bundle.getString("bagSizeId");
        bagSizeName = bundle.getString("bagSizeName");

        formEditTexts.get(2).setText(productName);


        searchableSpinners.get(0).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                plantSizeId = plantSizeIdList[position];
                plantSizeName = plantSizeNameList[position];
                try {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchableSpinners.get(1).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bagSizeId = bagSizeIdList[position];
                bagSizeName = bagSizeNameList[position];
                try {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MainPage.saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.setTitle("Amount is creating");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                for (int i=0;i<amountProductResponseList.size();i++){

                    addProductAmount(amountProductResponseList.get(i).getProductId(), amountProductResponseList.get(i).getProductSize(),
                            amountProductResponseList.get(i).getBagSize(), amountProductResponseList.get(i).getRetailPrice(), amountProductResponseList.get(i).getWholesalerPrice());

                }

                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                formEditTexts.get(0).setText("");
                formEditTexts.get(1).setText("");

            }
        });

        return view;

    }

    private void getPlantSize(String productId) {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getPlantSizeList(MainPage.userId, productId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                sizeResponseList = allList.getSizeResponseList();

                if (sizeResponseList.size()==0){

                    Toast.makeText(getActivity(), "No Plant Size", Toast.LENGTH_SHORT).show();
                }else {

                    plantSizeIdList = new String[sizeResponseList.size()];
                    plantSizeNameList = new String[sizeResponseList.size()];

                    for (int i=0;i<sizeResponseList.size();i++){

                        plantSizeIdList[i] = sizeResponseList.get(i).getSizeId();
                        plantSizeNameList[i] = sizeResponseList.get(i).getSizeName();

                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, plantSizeNameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    searchableSpinners.get(0).setAdapter(adapter);
                    if (plantSizeName!=null){
                        int spinnerPosition = adapter.getPosition(plantSizeName);
                        searchableSpinners.get(0).setSelection(spinnerPosition);
                    }
                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {

                Log.e("ListError", ""+t.getMessage());
            }
        });

    }

    private void getBagSizeList(String productId) {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getBagSizeList(MainPage.userId, productId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                bagSizeResponseList = allList.getBagSizeResponseList();

                if (bagSizeResponseList.size()==0){
                    Toast.makeText(getActivity(), "No Bag Size Found", Toast.LENGTH_SHORT).show();
                }else {

                    bagSizeIdList = new String[bagSizeResponseList.size()];
                    bagSizeNameList = new String[bagSizeResponseList.size()];

                    for (int i=0;i<bagSizeResponseList.size();i++){

                        bagSizeIdList[i] = bagSizeResponseList.get(i).getSizeId();
                        bagSizeNameList[i] = bagSizeResponseList.get(i).getSizeName();

                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, bagSizeNameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    searchableSpinners.get(1).setAdapter(adapter);
                    if (bagSizeName!=null){
                        int spinnerPosition = adapter.getPosition(bagSizeName);
                        searchableSpinners.get(1).setSelection(spinnerPosition);
                    }

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("ListError", ""+t.getMessage());
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:

                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity()){

                 //   addProductAmount(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString());

                }

                break;
        }
    }


    private void addProductAmount(String productId, String plantSizeId, String bagSizeId, String retailerPrice, String wholesalerPrice) {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.addProductAmount(MainPage.userId, productId, plantSizeId, bagSizeId, retailerPrice, wholesalerPrice);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().equals("true")){
                    Log.e("Response", ""+response.body().getMessage());
                } else if (response.body().getSuccess().equals("false")){
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainPage)getActivity()).loadFragment(new ProductList(), true);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
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
        MainPage.saveProduct.setVisibility(View.VISIBLE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getBagSizeList(productId);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}