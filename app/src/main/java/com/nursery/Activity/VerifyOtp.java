package com.nursery.Activity;


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
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.nursery.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VerifyOtp extends AppCompatActivity {

    @BindView(R.id.otpView)
    OtpView otpView;
    @BindView(R.id.verifyLayout)
    LinearLayout verifyLayout;
    public TextView submit, textView, txtreturncode;
    public String phoneNumber;
    public String code, OTP;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);
        //Permission's .....
        requestPermission();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        verifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                /*try {
                    code = otp;
                    int length = code.trim().length();
                    if (length == 4) {

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });

    }

    @OnClick({R.id.resendCode, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resendCode:
                break;

            case R.id.submit:

                Intent intent = new Intent(VerifyOtp.this, MainPage.class);
                startActivity(intent);
                finishAffinity();

               /* if (code != null) {
                    if (code.equalsIgnoreCase(OTP)) {
                        Success();
                    } else {
                        Toast.makeText(VerifyOtp.this, "OTP Not Match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(VerifyOtp.this, "Please Enter OTP", Toast.LENGTH_LONG).show();
                }*/

                break;


        }

    }

    private void Success() {

        ProgressDialog progressDialog = new ProgressDialog(VerifyOtp.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Account is in verification");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

    }

    private boolean validate(OtpView otpView) {
        if (otpView.getText().toString().trim().length() > 0) {
            return true;
        }

        Toast.makeText(VerifyOtp.this, "Please Enter OTP", Toast.LENGTH_LONG).show();

        return false;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyOtp.this);
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

