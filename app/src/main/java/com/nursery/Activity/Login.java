package com.nursery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nursery.Extra.Common;
import com.nursery.Model.LoginResponse;
import com.nursery.R;
import com.nursery.Retrofit.Api;
import com.nursery.Retrofit.ApiInterface;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    @BindViews({R.id.mobile})
    List<FormEditText> formEditTexts;
    @BindView(R.id.signIn)
    TextView signIn;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static final String INTENT_PHONENUMBER = "phonenumber";
    public static final String INTENT__CODE = "code";
    private TextWatcher mNumberTextWatcher;
    public String convert, OTP;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //Permission's .....
        requestPermission();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        File file = new File("data/data/com.nursery/shared_prefs/user.xml");

        if (file.exists()) {
            Intent intent = new Intent(Login.this, MainPage.class);
            startActivity(intent);
            finish();
        }

        formEditTexts.get(0).addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int length = formEditTexts.get(0).getText().toString().length();
                convert = String.valueOf(length);
                if (length > 9) {

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                } else {

                }

            }
        });

    }

    @OnClick({R.id.signIn})
    public void onClick() {

        if (formEditTexts.get(0).testValidity()) {

            ProgressDialog progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setTitle("Mobile Number is in verification");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.Login(formEditTexts.get(0).getText().toString().trim());
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    progressDialog.dismiss();
                    if (response.body().getSuccess().equals("true")) {

                        pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("UserLogin", "UserLoginSuccessful");
                        editor.commit();

                        Common.saveUserData(Login.this, "userId", response.body().getUserId());
                        Common.saveUserData(Login.this,"securityPin", response.body().getSecurityPin());
                        Intent intent = new Intent(Login.this, MainPage.class);
                        startActivity(intent);
                        finishAffinity();

                    } else if (response.body().getSuccess().equals("false")){
                        Toast.makeText(Login.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("Error", "" + t.getMessage());
                }
            });

        }

    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
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
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

