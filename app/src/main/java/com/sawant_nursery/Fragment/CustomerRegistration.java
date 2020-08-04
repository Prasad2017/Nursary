package com.sawant_nursery.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Adapter.CategoryTxtAdapter;
import com.sawant_nursery.Adapter.CityTxtAdapter;
import com.sawant_nursery.Adapter.StateTxtAdapter;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Extra.RecyclerTouchListener;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.CategoryResponse;
import com.sawant_nursery.Model.CityResponse;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.Model.StateResponse;
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
public class CustomerRegistration extends Fragment {

    View view;
    @BindViews({R.id.businessName, R.id.officeNumber, R.id.contactPerson, R.id.contactPersonNumber, R.id.email, R.id.businessAddress, R.id.city, R.id.state, R.id.pincode, R.id.gstNumber})
    List<FormEditText> formEditTexts;
    @BindView(R.id.customertypeSpin)
    Spinner customertypeSpin;
    String[] customerType = {"Retailer" ,"Wholesaler"};
    String type, cityId, cityName, stateId, stateName;
    @BindViews({R.id.statetxt, R.id.citytxt})
    List<TextView> textViews;
    RecyclerView recyclerView;
    TextView close;
    FormEditText searchEdit;
    List<StateResponse> stateResponseList = new ArrayList<>();
    List<StateResponse> searchStateResponseList = new ArrayList<>();
    List<CityResponse> cityResponseList = new ArrayList<>();
    List<CityResponse> searchCityResponseList = new ArrayList<>();



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_registration, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Add Customer");
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);


        if (MainPage.shopStatus.equals("Retailer_on")){
            type = "Retailer_on";
        } else if (MainPage.shopStatus.equals("Wholesaler_on")){
            type = "Wholesaler_on";
        } else if (MainPage.shopStatus.equals("Retailer_off") && MainPage.shopStatus.equals("Wholesaler_off")){
            Toast.makeText(getActivity(), "Please Select Shop Status in setting", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Please Select Shop Status in setting", Toast.LENGTH_SHORT).show();
        }

        return view;


    }

    @OnClick({R.id.citytxt, R.id.statetxt, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.citytxt:

                if (DetectConnection.checkInternetConnection(getActivity())) {

                    if (cityResponseList.size() > 0) {

                        Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.drop_down_list);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setCancelable(true);

                        recyclerView = dialog.findViewById(R.id.recyclerView);
                        close = dialog.findViewById(R.id.close);
                        searchEdit = dialog.findViewById(R.id.searchEdit);

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });

                        searchEdit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                                Log.d("text", "" + editable.toString());
                                String s = editable.toString();
                                searchCityResponseList = new ArrayList<>();
                                if (s.length() > 0) {
                                    for (int i = 0; i < cityResponseList.size(); i++)
                                        if (cityResponseList.get(i).getCity_name().toLowerCase().contains(s.toLowerCase().trim())) {
                                            searchCityResponseList.add(cityResponseList.get(i));
                                        }
                                    if (searchCityResponseList.size() < 1) {
                                        Toast.makeText(getActivity(), "Record Not Found", Toast.LENGTH_SHORT).show();
                                       /* CityTxtAdapter cityTxtAdapter = new CityTxtAdapter(getActivity(), cityResponseList);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        recyclerView.setAdapter(cityTxtAdapter);
                                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                                        cityTxtAdapter.notifyDataSetChanged();
                                        cityTxtAdapter.notifyItemInserted(cityResponseList.size() - 1);
                                        recyclerView.setHasFixedSize(true);*/
                                    } else {

                                    }
                                    Log.e("size", searchCityResponseList.size() + "" + cityResponseList.size());
                                } else {
                                    searchCityResponseList = new ArrayList<>();
                                    for (int i = 0; i < cityResponseList.size(); i++) {
                                        searchCityResponseList.add(cityResponseList.get(i));
                                    }
                                }

                                try {

                                    CityTxtAdapter cityTxtAdapter = new CityTxtAdapter(getActivity(), searchCityResponseList);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    recyclerView.setAdapter(cityTxtAdapter);
                                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                                    cityTxtAdapter.notifyDataSetChanged();
                                    cityTxtAdapter.notifyItemInserted(searchStateResponseList.size() - 1);
                                    recyclerView.setHasFixedSize(true);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                CityResponse cityResponse = cityResponseList.get(position);
                                textViews.get(1).setText(cityResponseList.get(position).getCity_name());
                                cityId = cityResponse.getCity_id();
                                cityName = cityResponse.getCity_name();

                                dialog.dismiss();
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }

                        }));

                        try {

                            CityTxtAdapter cityTxtAdapter = new CityTxtAdapter(getActivity(), cityResponseList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(cityTxtAdapter);
                            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                            cityTxtAdapter.notifyDataSetChanged();
                            cityTxtAdapter.notifyItemInserted(cityResponseList.size() - 1);
                            recyclerView.setHasFixedSize(true);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dialog.show();

                    } else {
                        textViews.get(1).setHint("Select City");
                        textViews.get(1).setText("");
                        Toast.makeText(getActivity(), "Select other state, No city found!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "No City Found", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.statetxt:

                if (DetectConnection.checkInternetConnection(getActivity())) {

                    if (stateResponseList.size() > 0) {

                        Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                        dialog.setContentView(R.layout.drop_down_list);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setCancelable(true);

                        recyclerView = dialog.findViewById(R.id.recyclerView);
                        close = dialog.findViewById(R.id.close);
                        searchEdit = dialog.findViewById(R.id.searchEdit);

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });

                        searchEdit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                                Log.d("text", "" + editable.toString());
                                String s = editable.toString();
                                searchStateResponseList = new ArrayList<>();
                                searchStateResponseList.clear();

                                if (s.length() > 0) {
                                    for (int i = 0; i < stateResponseList.size(); i++)
                                        if (stateResponseList.get(i).getState_name().toLowerCase().contains(s.toLowerCase().trim())) {
                                            searchStateResponseList.add(stateResponseList.get(i));
                                        }
                                    if (searchStateResponseList.size() < 1) {
                                        Toast.makeText(getActivity(), "Record Not Found", Toast.LENGTH_SHORT).show();

                                    } else {

                                    }
                                    Log.e("size", searchStateResponseList.size() + "" + stateResponseList.size());
                                } else {
                                    searchStateResponseList = new ArrayList<>();
                                    for (int i = 0; i < stateResponseList.size(); i++) {
                                        searchStateResponseList.add(stateResponseList.get(i));
                                    }
                                }

                                try {

                                    StateTxtAdapter stateTxtAdapter = new StateTxtAdapter(getActivity(), searchStateResponseList);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    recyclerView.setAdapter(stateTxtAdapter);
                                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                                    stateTxtAdapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                StateResponse stateResponse = stateResponseList.get(position);
                                textViews.get(0).setText(stateResponseList.get(position).getState_name());
                                stateId = stateResponse.getState_id();
                                stateName = stateResponse.getState_name();
                                getCityList(stateId);
                                dialog.dismiss();
                            }

                            @Override
                            public void onLongClick(View view, int position) {


                            }

                        }));

                        try {

                            StateTxtAdapter stateTxtAdapter = new StateTxtAdapter(getActivity(), stateResponseList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(stateTxtAdapter);
                            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                            stateTxtAdapter.notifyDataSetChanged();
                            stateTxtAdapter.notifyItemInserted(stateResponseList.size() - 1);
                            recyclerView.setHasFixedSize(true);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dialog.show();

                    } else {
                        textViews.get(0).setHint("Select State");
                        textViews.get(1).setHint("Select City");
                        textViews.get(0).setText("");
                        textViews.get(1).setText("");
                    }
                } else {
                    Toast.makeText(getActivity(), "No State Found", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.save:

                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity()
                        && formEditTexts.get(3).testValidity() && formEditTexts.get(4).testValidity() && formEditTexts.get(5).testValidity()
                        && formEditTexts.get(8).testValidity() && formEditTexts.get(9).testValidity()) {

                    if (!textViews.get(0).getText().toString().isEmpty()) {

                        if (!textViews.get(1).getText().toString().isEmpty()) {

                            AddCustomer(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString(), formEditTexts.get(2).getText().toString(),
                                    formEditTexts.get(3).getText().toString(), formEditTexts.get(4).getText().toString(), formEditTexts.get(5).getText().toString(),
                                    textViews.get(1).getText().toString(), textViews.get(0).getText().toString(), formEditTexts.get(8).getText().toString(), formEditTexts.get(9).getText().toString());

                        } else {
                            Toast.makeText(getActivity(), "Select City", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Select State", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }

    }

    private void getCityList(String stateId) {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getCityList(stateId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                cityResponseList = allList.getCityResponseList();
                if (cityResponseList.size()==0){
                    textViews.get(0).setHint("Select City");
                    textViews.get(0).setText("");
                    Toast.makeText(getActivity(), "Select other state, No city Found", Toast.LENGTH_SHORT).show();
                } else {

                    CityResponse cityResponse = cityResponseList.get(0);
                    cityId = cityResponseList.get(0).getCity_id();
                    cityName= cityResponseList.get(0).getCity_name();

                    textViews.get(1).setText(cityName);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    private void getStateList(){

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getStateList();
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                stateResponseList = allList.getStateResponseList();
                if (stateResponseList.size()==0){

                } else {

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    private void AddCustomer(String businessName, String officeNumber, String contactPerson, String PersonNumber, String email, String businessAddress, String city, String state, String pincode, String gst) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Account is in creating");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.addCustomer(MainPage.userId, type, businessName, officeNumber, contactPerson, PersonNumber, email, businessAddress, city, state, pincode, gst);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if (response.body().getSuccess().equals("true")){
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainPage)getActivity()).removeCurrentFragmentAndMoveBack();
                    ((MainPage)getActivity()).loadFragment(new CustomerList(), true);
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
            getStateList();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
