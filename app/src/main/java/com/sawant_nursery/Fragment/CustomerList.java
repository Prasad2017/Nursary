package com.sawant_nursery.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.sawant_nursery.Adapter.CustomerAdapter;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.CustomerResponse;
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
public class CustomerList extends Fragment {

    View view;
    @BindView(R.id.customerRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noCategorytxt)
    TextView noCategorytxt;
    @BindView(R.id.searchEdit)
    EditText searchEditText;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    List<CustomerResponse> customerResponseList = new ArrayList<>();
    List<CustomerResponse> searchcustomerResponseList = new ArrayList<>();
    CustomerAdapter adapter;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        ButterKnife.bind(this, view);
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
                if (editable.toString().length()>0) {
                    searchCustomer(editable.toString());
                }else {
                    getCustomerList();
                }
            }
        });

        return view;

    }

    @OnClick({R.id.addCustomer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCustomer:
                ((MainPage)getActivity()).loadFragment(new CustomerRegistration(), true);
                break;
        }

    }

    private void searchCustomer(String s) {

        searchcustomerResponseList = new ArrayList<>();
        searchcustomerResponseList.clear();

        if (s.length() > 0) {

            for (int i = 0; i < customerResponseList.size(); i++)
                if (customerResponseList.get(i).getCustomerName().toLowerCase().contains(s.toLowerCase().trim())) {
                    searchcustomerResponseList.add(customerResponseList.get(i));

                }

            if (searchcustomerResponseList.size() < 1) {

                noCategorytxt.setText("Record Not Found");
                noCategorytxt.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);

            } else {

                noCategorytxt.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }
        } else {

            searchcustomerResponseList = new ArrayList<>();
            for (int i = 0; i < customerResponseList.size(); i++) {
                searchcustomerResponseList.add(customerResponseList.get(i));
            }

        }

        setCustomerData();

    }

    private void setCustomerData() {

        adapter = new CustomerAdapter(getActivity(), searchcustomerResponseList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(searchcustomerResponseList.size() - 1);

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
            getCustomerList();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCustomerList() {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getCustomerList(MainPage.userId, MainPage.shopStatus);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                customerResponseList = allList.getCustomerResponseList();

                if (customerResponseList.size() == 0){
                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }else {

                    adapter = new CustomerAdapter(getActivity(), customerResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(customerResponseList.size() - 1);

                    noCategorytxt.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("CustomerError", ""+t.getMessage());
                noCategorytxt.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        });

    }
}
