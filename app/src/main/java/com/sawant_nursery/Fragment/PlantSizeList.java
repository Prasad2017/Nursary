package com.sawant_nursery.Fragment;

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
import com.sawant_nursery.Adapter.SizeAdapter;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.SizeResponse;
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


public class PlantSizeList extends Fragment {

    View view;
    @BindView(R.id.categoryRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noCategorytxt)
    TextView noCategorytxt;
    @BindView(R.id.searchEdit)
    EditText searchEditText;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    List<SizeResponse> sizeResponseList = new ArrayList<>();
    List<SizeResponse> searchsizeResponseList = new ArrayList<>();
    SizeAdapter adapter;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_size_list, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Size List");
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                if (editable.toString().length() > 0) {
                    searchProduct(editable.toString());
                } else {
                    noCategorytxt.setVisibility(View.VISIBLE);
                    noCategorytxt.setText("Search any plant size");
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });


        return view;
    }

    @OnClick({R.id.addSize})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addSize:
                ((MainPage)getActivity()).loadFragment(new AddPlantSize(), true);
                break;
        }

    }

    private void searchProduct(String s) {

        searchsizeResponseList = new ArrayList<>();
        searchsizeResponseList.clear();

        if (s.length() > 0) {

            for (int i = 0; i < sizeResponseList.size(); i++)
                if (sizeResponseList.get(i).getSizeName().toLowerCase().contains(s.toLowerCase().trim())) {
                    searchsizeResponseList.add(sizeResponseList.get(i));
                }

            if (searchsizeResponseList.size() < 1) {
                noCategorytxt.setText("plant size Not Found");
                noCategorytxt.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            } else {
                noCategorytxt.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } else {

            searchsizeResponseList = new ArrayList<>();
            for (int i = 0; i < sizeResponseList.size(); i++) {
                searchsizeResponseList.add(sizeResponseList.get(i));
            }

        }

        setProductsData();

    }

    private void setProductsData() {

        adapter = new SizeAdapter(getActivity(), searchsizeResponseList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(searchsizeResponseList.size() - 1);

    }

    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainPage.title.setVisibility(View.GONE);
        MainPage.importProduct.setVisibility(View.GONE);
        MainPage.titleLayout.setVisibility(View.VISIBLE);
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
        Call<AllList> call = apiInterface.getAllPlantSizeList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                sizeResponseList = allList.getSizeResponseList();

                if (sizeResponseList.size() == 0) {
                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                } else {

                    adapter = new SizeAdapter(getActivity(), sizeResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(sizeResponseList.size() - 1);
                    recyclerView.setHasFixedSize(true);

                    linearLayout.setVisibility(View.VISIBLE);
                    noCategorytxt.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                noCategorytxt.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                Log.e("ListError", "" + t.getMessage());
            }
        });

    }
}