package com.sawant_nursery.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.Blur;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.BagSizeResponse;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.Model.ProductResponse;
import com.sawant_nursery.Model.SizeResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
public class ProductDetails extends Fragment {

    View view;
    @BindViews({R.id.productName, R.id.totalAmount})
    List<TextView> textViews;
    @BindView(R.id.productImage)
    ImageView imageView;
    @BindView(R.id.quantity)
    FormEditText quantity;
    @BindView(R.id.productPrice)
    FormEditText productprice;
    @BindView(R.id.plantSize)
    Spinner plantSize;
    @BindView(R.id.bagSize)
    Spinner bagSize;
    @BindView(R.id.spinnerLayout)
    LinearLayout spinnerLayout;
    String productId, taxType, productName, productImage, customerId, customerType, customerName, plantSizeId, plantSizeName, bagSizeId, bagSizeName, cgst, sgst, igst;
    String[] productSizeIdList, productSizeNameList, bagSizeIdList, bagSizeNameList;
    List<SizeResponse> sizeResponseList = new ArrayList<>();
    List<BagSizeResponse> bagSizeResponseList = new ArrayList<>();
    List<ProductResponse> productResponseList = new ArrayList<>();



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Product Details");

        Bundle bundle = getArguments();
        customerId = bundle.getString("customerId")==null?"":bundle.getString("customerId");
        customerType = bundle.getString("customerType")==null?"":bundle.getString("customerType");
        customerName = bundle.getString("customerName")==null?"":bundle.getString("customerName");
        productId = bundle.getString("productId")==null?"":bundle.getString("productId");
        taxType = bundle.getString("taxType")==null?"":bundle.getString("taxType");
        Log.e("taxType", ""+taxType);
        productName = bundle.getString("productName")==null?"":bundle.getString("productName");
        productImage = bundle.getString("productImage")==null?"":bundle.getString("productImage");

        textViews.get(0).setText(productName);

        if (taxType.equalsIgnoreCase("Non-Taxable")){
            spinnerLayout.setVisibility(View.GONE);
            getAmount(productId);
        } else  if (taxType.equalsIgnoreCase("Taxable")){
            spinnerLayout.setVisibility(View.VISIBLE);
        }

        try {

            Transformation blurTransformation = new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    Bitmap blurred = Blur.fastblur(getActivity(), source, 10);
                    source.recycle();
                    return blurred;
                }

                @Override
                public String key() {
                    return "blur()";
                }
            };

            Picasso.with(getActivity())
                    .load("http://waghnursery.com/nursery/assets/img/"+productImage)
                    .placeholder(R.drawable.defaultimage)
                    .transform(blurTransformation)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            Picasso.with(getActivity())
                                    .load("http://waghnursery.com/nursery/assets/img/"+productImage)
                                    .placeholder(imageView.getDrawable())
                                    .into(imageView);

                        }

                        @Override
                        public void onError() {
                        }
                    });

        }catch (Exception e) {
            e.printStackTrace();
        }

        plantSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                plantSizeId = productSizeIdList[position];
                plantSizeName = productSizeNameList[position];
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

        bagSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bagSizeId = bagSizeIdList[position];
                bagSizeName = bagSizeNameList[position];
                try {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                    getProductPrice(bagSizeId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        productprice.addTextChangedListener(new TextWatcher() {
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
                    if (productprice.testValidity()){
                        if (quantity.testValidity()) {
                            if (Integer.parseInt(quantity.getText().toString().trim()) > 0) {

                                float price = Float.parseFloat(productprice.getText().toString());
                                float qnty = Float.parseFloat(quantity.getText().toString().trim());
                                float totalAmount = price * qnty;
                                textViews.get(1).setText("" + String.format(Locale.US, "%.2f", totalAmount));

                            } else {
                                quantity.setError("Enter Quantity");
                                quantity.requestFocus();
                                textViews.get(1).setText("0");
                            }
                        }
                    } else {
                        productprice.setError("Enter Price");
                        productprice.requestFocus();
                        textViews.get(1).setText("0");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        quantity.addTextChangedListener(new TextWatcher() {
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
                    if (productprice.testValidity()){
                        if (quantity.testValidity()) {
                            if (Integer.parseInt(quantity.getText().toString().trim()) > 0) {

                                float price = Float.parseFloat(productprice.getText().toString());
                                float qnty = Float.parseFloat(quantity.getText().toString().trim());
                                float totalAmount = price * qnty;
                                textViews.get(1).setText("" + String.format(Locale.US, "%.2f", totalAmount));

                            } else {
                                textViews.get(1).setText("0");
                            }
                        } else {
                            textViews.get(1).setText("0");
                        }
                    } else {
                        textViews.get(1).setText("0");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        return view;

    }

    private void getAmount(String productId) {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getProductWiseAmount(productId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                productResponseList = allList.getProductAmountResponseList();

                if (productResponseList.size()==0){
                    productprice.setText("0");
                } else {

                    for (int i=0;i<productResponseList.size();i++){

                        cgst = productResponseList.get(i).getCgst();
                        sgst = productResponseList.get(i).getSgst();
                        igst = productResponseList.get(i).getIgst();

                        if (customerType.equals("Retailer_on")) {
                            productprice.setText(productResponseList.get(i).getRetailPrice());
                        } else if (customerType.equals("Wholesaler_on")) {
                            productprice.setText(productResponseList.get(i).getWholesalerPrice());
                        }

                    }

                }


            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    private void getProductPrice(String bagSizeId) {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getProductPrice(MainPage.userId, productId, bagSizeId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                productResponseList = allList.getProductResponseList();

                if (productResponseList.size()==0){
                    productprice.setText("0");
                } else {

                    for (int i=0;i<productResponseList.size();i++){

                        cgst = productResponseList.get(i).getCgst();
                        sgst = productResponseList.get(i).getSgst();
                        igst = productResponseList.get(i).getIgst();

                        if (customerType.equals("Retailer_on")) {
                            productprice.setText(productResponseList.get(i).getRetailPrice());
                        } else if (customerType.equals("Wholesaler_on")) {
                            productprice.setText(productResponseList.get(i).getWholesalerPrice());
                        }

                    }

                }


            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    @SuppressLint("RtlHardcoded")
    @OnClick({R.id.addToCart})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.addToCart:
                if (quantity.testValidity() && productprice.testValidity()) {
                    if (DetectConnection.checkInternetConnection(getActivity())) {
                        AddOrder(productId, customerId, productprice.getText().toString().trim(), plantSizeName, bagSizeName, textViews.get(1).getText().toString().trim(), quantity.getText().toString().trim(), cgst, sgst, igst);
                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void AddOrder(String productId, String customerId, String productPrice, String plantSizeName, String bagSizeName, String totalAmount, String quantity, String cgst, String sgst, String igst) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Added into cart");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.addToCart(MainPage.userId, productId, customerId, productPrice, plantSizeName, bagSizeName, quantity, totalAmount, cgst, sgst, igst);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                Log.e("Response", ""+response.body());
                if (response.body().getSuccess().equals("true")){

                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainPage)getActivity()).removeCurrentFragmentAndMoveBack();

                    AllCategory allCategory = new AllCategory();
                    Bundle bundle = new Bundle();
                    bundle.putString("customerType", customerType);
                    bundle.putString("customerId", customerId);
                    bundle.putString("customerName", customerName);
                    allCategory.setArguments(bundle);
                    ((MainPage)getActivity()).loadFragment(allCategory, true);

                } else if (response.body().getSuccess().equals("false")){
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("CartError", ""+t.getMessage());
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
            getProductSize();
            getBagSize();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBagSize() {

        bagSizeResponseList.clear();

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getBagSizeList(MainPage.userId, productId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                bagSizeResponseList = allList.getBagSizeResponseList();

                if (bagSizeResponseList.size()==0){
                    Toast.makeText(getActivity(), "No bag size found", Toast.LENGTH_SHORT).show();
                } else {

                    bagSizeIdList = new String[bagSizeResponseList.size()];
                    bagSizeNameList = new String[bagSizeResponseList.size()];

                    for (int i = 0; i< bagSizeResponseList.size(); i++){

                        bagSizeIdList[i] = bagSizeResponseList.get(i).getSizeId();
                        bagSizeNameList[i] = bagSizeResponseList.get(i).getSizeName();

                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, bagSizeNameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bagSize.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    private void getProductSize() {

        sizeResponseList.clear();

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getPlantSizeList(MainPage.userId, productId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                sizeResponseList = allList.getSizeResponseList();

                if (sizeResponseList.size()==0){
                    Toast.makeText(getActivity(), "No plant size found", Toast.LENGTH_SHORT).show();
                } else {

                    productSizeIdList = new String[sizeResponseList.size()];
                    productSizeNameList = new String[sizeResponseList.size()];

                    for (int i = 0; i< sizeResponseList.size(); i++) {

                        productSizeIdList[i] = sizeResponseList.get(i).getSizeId();
                        productSizeNameList[i] = sizeResponseList.get(i).getSizeName();

                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, productSizeNameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    plantSize.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

}
