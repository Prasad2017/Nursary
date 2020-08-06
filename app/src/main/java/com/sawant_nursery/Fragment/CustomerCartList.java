package com.sawant_nursery.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Adapter.CartAdapter;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.CartResponse;
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
public class CustomerCartList extends Fragment {

    View view;
    @BindView(R.id.categoryRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.noCategorytxt)
    TextView noCategorytxt;
    @BindView(R.id.linearLayout)
    RelativeLayout linearLayout;
    List<CartResponse> cartResponseList = new ArrayList<>();
    public static CartAdapter adapter;
    String customerType, customerId,customerName, customerState;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        ButterKnife.bind(this, view);
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Bundle bundle = getArguments();
        customerType = bundle.getString("customerType");
        customerId = bundle.getString("customerId");
        customerName = bundle.getString("customerName");
        customerState = bundle.getString("customerState");
        try {
            MainPage.title.setText("" + customerName);
        }catch (Exception e){

        }
        return view;

    }

    @OnClick({R.id.checkout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkout:

                BillingDetails billingDetails = new BillingDetails();
                Bundle bundle = new Bundle();
                bundle.putString("customerType", customerType);
                bundle.putString("customerId", customerId);
                bundle.putString("customerName", customerName);
                billingDetails.setArguments(bundle);
                ((MainPage) getActivity()).loadFragment(billingDetails, true);

                break;
        }
    }

    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.titleLayout.setVisibility(View.VISIBLE);
        MainPage.importProduct.setVisibility(View.GONE);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.saveProduct.setVisibility(View.GONE);
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getCategoryList();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCategoryList() {
        cartResponseList.clear();
        recyclerView.clearOnScrollListeners();

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getCartProductList(MainPage.userId, customerId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                cartResponseList = allList.getCartResponseList();

                if (cartResponseList.size()==0){

                    noCategorytxt.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);

                  /*  ((MainPage) getActivity()).removeCurrentFragmentAndMoveBack();
                    AllCategory allCategory = new AllCategory();
                    Bundle bundle = new Bundle();
                    bundle.putString("customerType", customerType);
                    bundle.putString("customerId", customerId);
                    bundle.putString("customerName", customerName);
                    bundle.putString("customerState", customerState);
                    allCategory.setArguments(bundle);
                    ((MainPage) getActivity()).loadFragment(allCategory, true);*/

                }else {

                    adapter = new CartAdapter(getActivity(), cartResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(cartResponseList.size() - 1);
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
