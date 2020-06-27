package com.nursery.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nursery.Activity.MainPage;
import com.nursery.Adapter.SuperAdminProductAdapter;
import com.nursery.Extra.DetectConnection;
import com.nursery.Model.AllList;
import com.nursery.Model.LoginResponse;
import com.nursery.Model.ProductResponse;
import com.nursery.R;
import com.nursery.Retrofit.Api;
import com.nursery.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuperAdminProductList extends Fragment {

    View view;
    @BindView(R.id.categoryRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noCategorytxt)
    TextView noCategorytxt;
    @BindView(R.id.searchEdit)
    EditText searchEditText;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    List<ProductResponse> categoryResponseList = new ArrayList<>();
    List<ProductResponse> searchcategoryResponseList = new ArrayList<>();
    SuperAdminProductAdapter adapter;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_super_admin_product_list, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Admin Product's");
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(linearLayout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("text", editable.toString());
                if (editable.toString().length()>0) {
                    searchProduct(editable.toString());
                }else {
                    noCategorytxt.setVisibility(View.VISIBLE);
                    noCategorytxt.setText("Search any product.....");
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });

        MainPage.importProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DetectConnection.checkInternetConnection(getActivity())) {

                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setTitle("Product is in creating");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    for (int i = 0; i < SuperAdminProductAdapter.categoryResponseList.size(); i++) {
                        if (SuperAdminProductAdapter.categoryResponseList.get(i).getSelected()) {
                            Log.e("TAG", "Present" + SuperAdminProductAdapter.categoryResponseList.get(i).getProductId());
                            addProduct(SuperAdminProductAdapter.categoryResponseList.get(i).getProductId());
                        }
                    }

                    progressDialog.dismiss();

                }else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }

    private void addProduct(String productId) {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.addAdminProduct(MainPage.userId, productId);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().equals("true")){
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else if (response.body().getSuccess().equals("false")){
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    private void searchProduct(String s) {

        searchcategoryResponseList = new ArrayList<>();
        searchcategoryResponseList.clear();

        if (s.length() > 0) {

            for (int i = 0; i < categoryResponseList.size(); i++)
                if (categoryResponseList.get(i).getProductName().toLowerCase().contains(s.toLowerCase().trim())) {
                    searchcategoryResponseList.add(categoryResponseList.get(i));
                }

            if (searchcategoryResponseList.size() < 1) {
                noCategorytxt.setText("Product Not Found");
                noCategorytxt.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            } else {
                noCategorytxt.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } else {

            searchcategoryResponseList = new ArrayList<>();
            for (int i = 0; i < categoryResponseList.size(); i++) {
                searchcategoryResponseList.add(categoryResponseList.get(i));
            }

        }

        setProductsData();

    }

    private void setProductsData() {

        adapter = new SuperAdminProductAdapter(getActivity(), searchcategoryResponseList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(searchcategoryResponseList.size() - 1);

    }

    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainPage.title.setVisibility(View.GONE);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.titleLayout.setVisibility(View.VISIBLE);
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
        Call<AllList> call = apiInterface.getadminProductList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                categoryResponseList = allList.getProductResponseList();

                if (categoryResponseList.size()==0){
                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    MainPage.importProduct.setVisibility(View.GONE);
                }else {

                    adapter = new SuperAdminProductAdapter(getActivity(), categoryResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(categoryResponseList.size() - 1);
                    recyclerView.setHasFixedSize(true);

                    linearLayout.setVisibility(View.VISIBLE);
                    noCategorytxt.setVisibility(View.GONE);
                    MainPage.importProduct.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                noCategorytxt.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                Log.e("ListError", ""+t.getMessage());
            }
        });

    }

}

