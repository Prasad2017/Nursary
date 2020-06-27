package com.nursery.Fragment;

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
import com.nursery.Adapter.ProductSelectionAdapter;
import com.nursery.Extra.DetectConnection;
import com.nursery.Model.AllList;
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
public class ProductList extends Fragment {

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



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Product's");
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(searchcategoryResponseList.size() - 1);

    }

    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainPage.title.setVisibility(View.GONE);
        MainPage.importProduct.setVisibility(View.GONE);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.titleLayout.setVisibility(View.VISIBLE);
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
        Call<AllList> call = apiInterface.getProductList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                categoryResponseList = allList.getProductResponseList();

                if (categoryResponseList.size()==0){
                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }else {

                    adapter = new ProductSelectionAdapter(getActivity(), categoryResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(categoryResponseList.size() - 1);
                    recyclerView.setHasFixedSize(true);

                    linearLayout.setVisibility(View.VISIBLE);
                    noCategorytxt.setVisibility(View.GONE);

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
