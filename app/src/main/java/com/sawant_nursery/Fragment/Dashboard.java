package com.sawant_nursery.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Adapter.ProductAdapter;
import com.sawant_nursery.Adapter.SlidingImage_Adapter;
import com.sawant_nursery.Extra.Common;
import com.sawant_nursery.Extra.DetectConnection;
import com.sawant_nursery.Model.AllList;
import com.sawant_nursery.Model.ProductResponse;
import com.sawant_nursery.Model.Viewpager;
import com.sawant_nursery.Model.ViewpagerResponce;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    View view;
    @BindView(R.id.productRecyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.searchEditText)
    EditText searchEditText;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.sliderLayout)
    LinearLayout sliderLayout;
    @BindView(R.id.viewAll)
    TextView viewAll;
    List<ProductResponse> categoryResponseList = new ArrayList<>();
    List<ProductResponse> searchcategoryResponseList = new ArrayList<>();
    ProductAdapter adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ArrayList<Viewpager> arr = new ArrayList<>();
    CirclePageIndicator indicator;
    public static ViewPager mPager;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.indicator);
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (MainPage.shopStatus.equals("Retailer_on")){
            MainPage.adminStatus.setText("Garden center");
            MainPage.adminStatus.setVisibility(View.VISIBLE);
            MainPage.adminStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.retail_icon), null, null, null);
        } else if (MainPage.shopStatus.equals("Wholesaler_on")){
            MainPage.adminStatus.setText("Nursery");
            MainPage.adminStatus.setVisibility(View.VISIBLE);
            MainPage.adminStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),R.drawable.distributor_icon), null, null, null);
        } else {
           MainPage.adminStatus.setVisibility(View.GONE);
        }

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
                    getProductList();
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

            } else {
                recyclerView.setVisibility(View.VISIBLE);
            }
        } else {

            searchcategoryResponseList = new ArrayList<>();
            for (int i = 0; i < categoryResponseList.size(); i++) {
                searchcategoryResponseList.add(categoryResponseList.get(i));
            }

        }

        setProductData();

    }

    private void setProductData() {

        adapter = new ProductAdapter(getActivity(), searchcategoryResponseList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(searchcategoryResponseList.size() - 1);

    }

    @OnClick({R.id.invoice, R.id.customerRegistration, R.id.viewAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customerRegistration:
                ((MainPage) getActivity()).loadFragment(new CustomerList(), true);
                break;

            case R.id.invoice:
                ((MainPage) getActivity()).loadFragment(new CustomerSelection(), true);
                break;

            case R.id.viewAll:

                break;
        }
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
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())){
            requestPermission();
            getViewpager();
            getProductList();
            Common.getProfile(getActivity());
        }else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {

        Dexter.withActivity(getActivity())
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void getViewpager() {

        ApiInterface api = Api.getClient().create(ApiInterface.class);
        Call<ViewpagerResponce> call = api.getSliderList();
        call.enqueue(new Callback<ViewpagerResponce>() {
            @Override
            public void onResponse(Call<ViewpagerResponce> call, Response<ViewpagerResponce> response) {
                try {
                    Log.e("response==", String.valueOf(response.body().getViewpagerList()));
                    arr = (ArrayList<Viewpager>) response.body().getViewpagerList();
                    Log.e("Arraylist==", "" + arr);
                    if (arr.size() > 0) {
                        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), arr));
                        indicator.setViewPager(mPager);
                        sliderLayout.setVisibility(View.VISIBLE);
                    } else {
                        sliderLayout.setVisibility(View.GONE);
                    }

                    final float density = getResources().getDisplayMetrics().density;
                    indicator.setRadius(5 * density);
                    //Set circle indicator radius
                    indicator.setRadius(5 * density);

                    NUM_PAGES = arr.size();
                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {

                            if (currentPage == NUM_PAGES) {
                                currentPage = 0;
                            }
                            mPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 10000, 10000);

                    // Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }

                        @Override
                        public void onPageScrolled(int position, float arg1, int arg2) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int position) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ViewpagerResponce> call, Throwable t) {
                // Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
                Log.e("Error:", t.getMessage());
            }
        });
    }


    private void getProductList() {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getRandomProductList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                categoryResponseList = allList.getProductResponseList();

                if (categoryResponseList.size()==0){
                    cardView.setVisibility(View.GONE);
                }else {

                    if (categoryResponseList.size()>10){
                        viewAll.setVisibility(View.VISIBLE);
                    } else {
                        viewAll.setVisibility(View.GONE);
                    }

                    adapter = new ProductAdapter(getActivity(), categoryResponseList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemInserted(categoryResponseList.size() - 1);
                    recyclerView.setHasFixedSize(true);

                    cardView.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                cardView.setVisibility(View.GONE);
                Log.e("ListError", ""+t.getMessage());
            }
        });

    }

}
