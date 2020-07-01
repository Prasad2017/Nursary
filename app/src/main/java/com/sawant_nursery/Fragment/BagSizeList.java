package com.sawant_nursery.Fragment;

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

import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Adapter.BagSizeAdapter;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.BagSizeResponse;
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


public class BagSizeList extends Fragment {

    View view;
    @BindView(R.id.categoryRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noCategorytxt)
    TextView noCategorytxt;
    @BindView(R.id.searchEdit)
    EditText searchEditText;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    List<BagSizeResponse> bagSizeResponseList = new ArrayList<>();
    List<BagSizeResponse> searchbagSizeResponseList = new ArrayList<>();
    BagSizeAdapter adapter;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bag_size_list, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Size List");
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
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
                ((MainPage)getActivity()).loadFragment(new AddBagSize(), true);
                break;
        }

    }

    private void searchProduct(String s) {

        searchbagSizeResponseList = new ArrayList<>();
        searchbagSizeResponseList.clear();

        if (s.length() > 0) {

            for (int i = 0; i < bagSizeResponseList.size(); i++)
                if (bagSizeResponseList.get(i).getSizeName().toLowerCase().contains(s.toLowerCase().trim())) {
                    searchbagSizeResponseList.add(bagSizeResponseList.get(i));
                }

            if (searchbagSizeResponseList.size() < 1) {
                noCategorytxt.setText("plant size Not Found");
                noCategorytxt.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            } else {
                noCategorytxt.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } else {

            searchbagSizeResponseList = new ArrayList<>();
            for (int i = 0; i < bagSizeResponseList.size(); i++) {
                searchbagSizeResponseList.add(bagSizeResponseList.get(i));
            }

        }

        setProductsData();

    }

    private void setProductsData() {

        adapter = new BagSizeAdapter(getActivity(), searchbagSizeResponseList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(searchbagSizeResponseList.size() - 1);

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
        Call<AllList> call = apiInterface.getAllBagSizeList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                bagSizeResponseList = allList.getBagSizeResponseList();

                if (bagSizeResponseList.size() == 0) {
                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                } else {

                    adapter = new BagSizeAdapter(getActivity(), bagSizeResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(bagSizeResponseList.size() - 1);
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