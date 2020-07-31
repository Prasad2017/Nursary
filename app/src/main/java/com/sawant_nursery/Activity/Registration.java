package com.sawant_nursery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.andreabaccega.widget.FormEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sawant_nursery.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class Registration extends AppCompatActivity {

    @BindViews({R.id.shopName, R.id.officeNumber, R.id.contactPerson, R.id.personNumber, R.id.gstNumber, R.id.emailId, R.id.businessAddress})
    List<FormEditText> formEditTexts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

    }
}