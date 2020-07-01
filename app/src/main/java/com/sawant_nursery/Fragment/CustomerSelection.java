package com.sawant_nursery.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.CustomerResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

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
public class CustomerSelection extends Fragment {


    View view;
    @BindView(R.id.addCustomer)
    TextView addCustomer;
    @BindViews({R.id.customertypeSpin, R.id.customerNameSpin})
    List<Spinner> searchableSpinners;
    String[] customerType = {"Retailer" ,"Wholesaler"};
    List<CustomerResponse> customerResponseList = new ArrayList<>();
    String[] customerIdList, customerNameList;
    String type="", customerId="", customerName="";



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_selection, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Customer Selection");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, customerType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchableSpinners.get(0).setAdapter(adapter);

        SpannableString spannableString = new SpannableString("Add Customer");
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        addCustomer.setText(spannableString);

        /*searchableSpinners.get(0).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                type = customerType[position];
                Log.e("documentType", ""+type);
                try {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                    getCustomerList(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        searchableSpinners.get(1).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                customerId = customerIdList[position];
                customerName = customerNameList[position];
                Log.e("customerId", ""+customerId);
                Log.e("customerName", ""+customerName);
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

        if (MainPage.shopStatus.equals("Retailer_on")){
            type = "Retailer_on";
            getCustomerList(type);
        } else if (MainPage.shopStatus.equals("Retailer_off")){

        } else if (MainPage.shopStatus.equals("Wholesaler_on")){
            type = "Wholesaler_on";
            getCustomerList(type);
        } else if (MainPage.shopStatus.equals("Wholesaler_off")){

        } else if (MainPage.shopStatus.equals("Retailer_off") && MainPage.shopStatus.equals("Wholesaler_off")){
            Toast.makeText(getActivity(), "Please Select Shop Status in setting", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Please Select Shop Status in setting", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @SuppressLint("RtlHardcoded")
    @OnClick({R.id.customerSelection, R.id.addCustomer})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.addCustomer:
                ((MainPage)getActivity()).loadFragment(new CustomerRegistration(), true);
                break;

            case R.id.customerSelection:

                if (!customerId.equals("") || !customerId.isEmpty()) {

                    AllCategory allCategory = new AllCategory();
                    Bundle bundle = new Bundle();
                    bundle.putString("customerType", type);
                    bundle.putString("customerId", customerId);
                    bundle.putString("customerName", customerName);
                    allCategory.setArguments(bundle);
                    ((MainPage) getActivity()).loadFragment(allCategory, true);

                } else {
                    Toast.makeText(getActivity(), "Please Select Customer", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainPage.title.setVisibility(View.GONE);
        MainPage.titleLayout.setVisibility(View.VISIBLE);
        MainPage.importProduct.setVisibility(View.GONE);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.saveProduct.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())) {
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCustomerList(String type) {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getTypeCustomerList(MainPage.userId, type);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                customerResponseList = allList.getCustomerResponseList();

                if (customerResponseList.size()==0){
                    Toast.makeText(getActivity(), "No customer found", Toast.LENGTH_SHORT).show();
                }else {

                    customerIdList = new String[customerResponseList.size()];
                    customerNameList = new String[customerResponseList.size()];

                    for (int i=0;i<customerResponseList.size();i++){

                        customerIdList[i] = customerResponseList.get(i).getCustomerId();
                        customerNameList[i] = customerResponseList.get(i).getCustomerName();

                    }

                    final ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, customerNameList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
                    searchableSpinners.get(1).setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("CustomerError", ""+t.getMessage());
            }
        });

    }
}
