package com.nursery.Fragment;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nursery.Activity.MainPage;
import com.nursery.Adapter.CustomerAdapter;
import com.nursery.Adapter.InvoiceAdapter;
import com.nursery.Extra.DetectConnection;
import com.nursery.Model.AllList;
import com.nursery.Model.CustomerResponse;
import com.nursery.Model.InvoiceResponse;
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


public class InvoiceList extends Fragment {

    View view;
    @BindView(R.id.customerRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noCategorytxt)
    TextView noCategorytxt;
    @BindView(R.id.searchEdit)
    EditText searchEditText;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
    List<InvoiceResponse> searchcustomerResponseList = new ArrayList<>();
    InvoiceAdapter adapter;
    String customerId;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invoice_list, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        customerId = bundle.getString("customerId");

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
                    searchInvoice(editable.toString());
                }else {
                    noCategorytxt.setVisibility(View.VISIBLE);
                    noCategorytxt.setText("Search any customer.....");
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });

        return view;

    }

    private void searchInvoice(String s) {

        searchcustomerResponseList = new ArrayList<>();
        searchcustomerResponseList.clear();

        if (s.length() > 0) {

            for (int i = 0; i < invoiceResponseList.size(); i++)
                if (invoiceResponseList.get(i).getOrder_number().toLowerCase().contains(s.toLowerCase().trim())) {
                    searchcustomerResponseList.add(invoiceResponseList.get(i));

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
            for (int i = 0; i < invoiceResponseList.size(); i++) {
                searchcustomerResponseList.add(invoiceResponseList.get(i));
            }

        }

        setCustomerData();

    }

    private void setCustomerData() {

        adapter = new InvoiceAdapter(getActivity(), searchcustomerResponseList);
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
            getInvoiceList();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getInvoiceList() {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getInvoiceList(MainPage.userId, customerId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                invoiceResponseList = allList.getInvoiceResponseList();

                if (invoiceResponseList.size() == 0){
                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }else {

                    adapter = new InvoiceAdapter(getActivity(), invoiceResponseList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(invoiceResponseList.size() - 1);

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