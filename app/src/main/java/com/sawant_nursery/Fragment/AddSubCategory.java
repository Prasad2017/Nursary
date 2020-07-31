package com.sawant_nursery.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.CategoryResponse;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSubCategory extends Fragment {

    View view;
    @BindView(R.id.productCategory)
    FormEditText productCategory;
    @BindView(R.id.spinProductCategory)
    Spinner categorySpinner;
    List<CategoryResponse> categoryResponseList = new ArrayList<>();
    String[] categoryIdList, categoryNameList;
    String categoryId, categoryName;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_category, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("");

        productCategory.setLongClickable(false);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                categoryId = categoryIdList[position];
                try {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;

    }

    @OnClick({R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:

                if (productCategory.testValidity()){
                    addProductSize(productCategory.getText().toString());
                }

                break;
        }
    }

    private void addProductSize(String prodCategory) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Sub Category is in creating");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.addProductCategory(MainPage.userId, categoryId, prodCategory);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().equals("true")){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    productCategory.setText("");
                    ((MainPage) getActivity()).removeCurrentFragmentAndMoveBack();
                    ((MainPage) getActivity()).loadFragment(new CategoryList(), true);
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
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.saveProduct.setVisibility(View.GONE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getCategoryList();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCategoryList() {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getCategoryList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                categoryResponseList = allList.getCategoryResponseList();

                if (categoryResponseList.size()<1){

                } else {

                    categoryIdList = new String[categoryResponseList.size()];
                    categoryNameList = new String[categoryResponseList.size()];

                    for (int i=0;i<categoryResponseList.size();i++){

                        categoryIdList[i] = categoryResponseList.get(i).getCategoryId();
                        categoryNameList[i] = categoryResponseList.get(i).getCategoryType();

                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categoryNameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });


    }

}
