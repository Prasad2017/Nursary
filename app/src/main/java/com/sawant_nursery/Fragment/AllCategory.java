package com.sawant_nursery.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Adapter.ProductSelectionAdapter;
import com.sawant_nursery.Extra.Common;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Extra.RecyclerTouchListener;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.ProductResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;

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
public class AllCategory extends Fragment {

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
    ProductSelectionAdapter adapter;
    String customerId, customerType, customerName;
    Bundle bundle;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Product List");
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        bundle = getArguments();
        customerType = bundle.getString("customerType");
        customerId = bundle.getString("customerId");
        customerName = bundle.getString("customerName");

        MainPage.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerCartList customerCartList = new CustomerCartList();
                Bundle bundle = new Bundle();
                bundle.putString("customerType", customerType);
                bundle.putString("customerId", customerId);
                bundle.putString("customerName", customerName);
                customerCartList.setArguments(bundle);
                ((MainPage)getActivity()).loadFragment(customerCartList, true);

            }
        });

        MainPage.cartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerCartList customerCartList = new CustomerCartList();
                Bundle bundle = new Bundle();
                bundle.putString("customerType", customerType);
                bundle.putString("customerId", customerId);
                bundle.putString("customerName", customerName);
                customerCartList.setArguments(bundle);
                ((MainPage)getActivity()).loadFragment(customerCartList, true);

            }
        });

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
                    getCategoryList();
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ProductResponse productResponse = categoryResponseList.get(position);

                String price="0";

                ProductDetails productDetails = new ProductDetails();
                bundle = new Bundle();
                bundle.putString("customerId", customerId);
                bundle.putString("customerType", customerType);
                bundle.putString("customerName", customerName);
                bundle.putString("productId", categoryResponseList.get(position).getProductId());
                bundle.putString("taxType", categoryResponseList.get(position).getTaxType());
                bundle.putString("productName", categoryResponseList.get(position).getProductName());
                bundle.putString("productImage", categoryResponseList.get(position).getCategoryImage());
                productDetails.setArguments(bundle);
                ((MainPage)getActivity()).loadFragment(productDetails, true);

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        return view;

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

        adapter = new ProductSelectionAdapter(getActivity(), searchcategoryResponseList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(searchcategoryResponseList.size() - 1);

    }

    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainPage.title.setVisibility(View.GONE);
        MainPage.titleLayout.setVisibility(View.VISIBLE);
        MainPage.importProduct.setVisibility(View.GONE);
        MainPage.cart.setVisibility(View.VISIBLE);
        MainPage.cartCount.setVisibility(View.VISIBLE);
        MainPage.saveProduct.setVisibility(View.GONE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getCategoryList();
            Common.getCartCount(getActivity(), customerId);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCategoryList() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getProductList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                categoryResponseList = allList.getProductResponseList();

                if (categoryResponseList.size()==0){
                    progressDialog.dismiss();
                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    MainPage.cart.setVisibility(View.GONE);
                    MainPage.cartCount.setVisibility(View.GONE);
                }else {

                    adapter = new ProductSelectionAdapter(getActivity(), categoryResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(categoryResponseList.size() - 1);
                    recyclerView.setHasFixedSize(true);

                    linearLayout.setVisibility(View.VISIBLE);
                    noCategorytxt.setVisibility(View.GONE);
                    MainPage.cartCount.setVisibility(View.VISIBLE);
                    MainPage.cart.setVisibility(View.VISIBLE);

                    //set progressDialog
                    progressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                progressDialog.dismiss();
                noCategorytxt.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                MainPage.cartCount.setVisibility(View.GONE);
                MainPage.cart.setVisibility(View.GONE);
                Log.e("ListError", ""+t.getMessage());
            }
        });

    }

}
