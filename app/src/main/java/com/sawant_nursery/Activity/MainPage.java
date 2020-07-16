package com.sawant_nursery.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.sawant_nursery.Extra.Common;
import com.sawant_nursery.Fragment.AddAmount;
import com.sawant_nursery.Fragment.AddProduct;
import com.sawant_nursery.Fragment.AddSecurityPin;
import com.sawant_nursery.Fragment.BagSizeList;
import com.sawant_nursery.Fragment.CategoryList;
import com.sawant_nursery.Fragment.CustomerList;
import com.sawant_nursery.Fragment.CustomerRegistration;
import com.sawant_nursery.Fragment.CustomerSelection;
import com.sawant_nursery.Fragment.Dashboard;
import com.sawant_nursery.Fragment.InvoiceList;
import com.sawant_nursery.Fragment.LedgerList;
import com.sawant_nursery.Fragment.PlantSizeList;
import com.sawant_nursery.Fragment.ValidatePin;
import com.sawant_nursery.R;
import com.sawant_nursery.View.ChildModel;
import com.sawant_nursery.View.ExpandableNavigationListView;
import com.sawant_nursery.View.HeaderModel;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainPage extends AppCompatActivity {

    String from;
    public static ImageView menu, back, cart, logo;
    public static DrawerLayout drawerLayout;
    public static TextView title, cartCount, editProfile, importProduct, saveProduct;
    boolean doubleBackToExitPressedOnce = false;
    public static View toolbar;
    public static ProgressBar progressBar;
    public static RelativeLayout titleLayout;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    public static LinearLayout toolbarContainer;
    public ExpandableNavigationListView navigationExpandableListView;
    public static String userId, name, mobileNumber, securityPin, subAmount, currency="₹", shopStatus;
    public static TextView phone, uname, editprofile, adminStatus;
    public static ImageView userImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        toolbarContainer = findViewById(R.id.toolbar_container);
        try {
            userId = Common.getSavedUserData(MainPage.this, "userId");
            Log.d("userId", userId);
            name = Common.getSavedUserData(MainPage.this, "name");
            shopStatus = Common.getSavedUserData(MainPage.this, "shopStatus");
            securityPin = Common.getSavedUserData(MainPage.this, "securityPin");
        }catch (Exception e){
            e.printStackTrace();
        }

        loadFragment(new Dashboard(), false);
        NavigationView navigationView = findViewById(R.id.navigationView);
        View hView = navigationView.getHeaderView(0);
        adminStatus = hView.findViewById(R.id.adminStatus);

        //ExpandList Navigation view
        navigationExpandableListView
                .init(MainPage.this)
                .addHeaderModel(new HeaderModel("Dashboard", false))
                .addHeaderModel(new HeaderModel("Master", true)
                        .addChildModel(new ChildModel("Category"))
                        .addChildModel(new ChildModel("Plant Size"))
                        .addChildModel(new ChildModel("Bag Size"))
                        .addChildModel(new ChildModel("Product"))
                        .addChildModel(new ChildModel("Product Price")))
                .addHeaderModel(new HeaderModel("Customer", true)
                        .addChildModel(new ChildModel("Add Customer"))
                        .addChildModel(new ChildModel("Customer List")))
                .addHeaderModel(new HeaderModel("Invoice", true)
                        .addChildModel(new ChildModel("Add Invoice"))
                        .addChildModel(new ChildModel("Invoice History")))
                .addHeaderModel(new HeaderModel("Setting", false))
                .build()
                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        navigationExpandableListView.setSelected(groupPosition);

                        if (id == 0) {
                            loadFragment(new Dashboard(), false);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else if (id == 4) {
                            if (!securityPin.equals("0")) {
                                loadFragment(new ValidatePin(), true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                            } else {
                                loadFragment(new AddSecurityPin(), true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                        }

                        return false;
                    }
                })
                .addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        if (groupPosition == 1  && id == 0) {
                            loadFragment(new CategoryList(), true);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 1  && id == 1) {
                            loadFragment(new PlantSizeList(), true);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 1  && id == 2) {
                            loadFragment(new BagSizeList(), true);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 1  && id == 3) {
                            loadFragment(new AddProduct(), true);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 1  && id == 4) {
                            loadFragment(new AddAmount(), true);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 2  && id == 0) {
                            loadFragment(new CustomerRegistration(), true);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 2  && id == 1) {
                            loadFragment(new CustomerList(), true);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else if (groupPosition == 3  && id == 0) {
                            loadFragment(new CustomerSelection(), true);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        return false;
                    }
                });


    }

    private void logout() {

        final Dialog dialog = new Dialog(MainPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView txtyes = dialog.findViewById(R.id.yes);
        TextView txtno = dialog.findViewById(R.id.no);

        txtno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Common.saveUserData(MainPage.this, "userId", "");
                File file1 = new File("data/data/com.sawant_nursery/shared_prefs/user.xml");
                if (file1.exists()) {
                    file1.delete();
                }

                Intent intent = new Intent(MainPage.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        dialog.show();

    }

    @OnClick({R.id.menu, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (!MainPage.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    MainPage.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.back:
                removeCurrentFragmentAndMoveBack();
                break;
        }
    }

    private void initViews() {

        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.title);
        titleLayout = findViewById(R.id.titleLayout);
        menu = findViewById(R.id.menu);
        back = findViewById(R.id.back);
        cart = findViewById(R.id.cart);
        cartCount = findViewById(R.id.cartCount);
        saveProduct = findViewById(R.id.saveProduct);
        progressBar = findViewById(R.id.progressBar);
        importProduct = findViewById(R.id.importProduct);
        navigationExpandableListView = findViewById(R.id.expandable_navigation);

    }

    public void removeCurrentFragmentAndMoveBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        // double press to exit
        if (menu.getVisibility() == View.VISIBLE) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
        } else {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public void lockUnlockDrawer(int lockMode) {
        drawerLayout.setDrawerLockMode(lockMode);
        if (lockMode == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            menu.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);

        } else {
            menu.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
        }
    }

}

