package com.nursery.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.aminography.choosephotohelper.callback.ChoosePhotoCallback;
import com.andreabaccega.widget.FormEditText;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nursery.Activity.MainPage;
import com.nursery.Extra.DetectConnection;
import com.nursery.Model.AllList;
import com.nursery.Model.BagSizeResponse;
import com.nursery.Model.CategoryResponse;
import com.nursery.Model.LoginResponse;
import com.nursery.Model.SizeResponse;
import com.nursery.R;
import com.nursery.Retrofit.Api;
import com.nursery.Retrofit.ApiInterface;

import java.io.ByteArrayOutputStream;
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
public class AddProduct extends Fragment {

    View view;
    @BindViews({R.id.botanicalName, R.id.productName, R.id.cgst, R.id.sgst, R.id.igst})
    List<FormEditText> formEditTexts;
    @BindView(R.id.productSize)
    TextView productSize;
    @BindView(R.id.productBagSize)
    TextView productBagSize;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.productCategory)
    Spinner spinner;
    @BindView(R.id.taxType)
    Spinner taxTypeSpin;
    private ChoosePhotoHelper choosePhotoHelper;
    Bitmap bitmap;
    String productImage, categoryId, categoryName, prodsizeId, productbagId;
    String[]categoryIdList, categoryNameList, productSizeIdList, productSizeNameList, productBageIdList, productBagSizeList;
    List<CategoryResponse> categoryResponseList = new ArrayList<>();
    List<SizeResponse> sizeResponseList = new ArrayList<>();
    List<BagSizeResponse> bagSizeResponseList = new ArrayList<>();
    protected ArrayList<String> productSizeId = new ArrayList<String>();
    protected ArrayList<String> productSizeName = new ArrayList<String>();
    protected ArrayList<String> productBagSizeId = new ArrayList<String>();
    protected ArrayList<String> productBagSizeName = new ArrayList<String>();
    String[] tax_type = {"Taxable" ,"Non-Taxable"};
    String taxType;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_product, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("");

        formEditTexts.get(0).setSelection(formEditTexts.get(0).getText().toString().length());
        formEditTexts.get(1).setSelection(formEditTexts.get(1).getText().toString().length());
        formEditTexts.get(2).setSelection(formEditTexts.get(2).getText().toString().length());
        formEditTexts.get(3).setSelection(formEditTexts.get(3).getText().toString().length());
        formEditTexts.get(4).setSelection(formEditTexts.get(4).getText().toString().length());

        formEditTexts.get(0).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        formEditTexts.get(1).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, tax_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taxTypeSpin.setAdapter(adapter);

        taxTypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                taxType = tax_type[position];
                Log.e("taxType", ""+taxType);
                try {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));

                    if (taxType.equals("Taxable")){

                        formEditTexts.get(2).setVisibility(View.VISIBLE);
                        formEditTexts.get(3).setVisibility(View.VISIBLE);
                        formEditTexts.get(4).setVisibility(View.VISIBLE);

                    } else {

                        formEditTexts.get(2).setVisibility(View.GONE);
                        formEditTexts.get(3).setVisibility(View.GONE);
                        formEditTexts.get(4).setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        choosePhotoHelper = ChoosePhotoHelper.with(this)
                .asFilePath()
                .build(new ChoosePhotoCallback<String>() {
                    @Override
                    public void onChoose(String photo) {
                        Glide.with(imageView)
                                .load(photo)
                                .apply(RequestOptions.placeholderOf(R.drawable.defaultimage))
                                .into(imageView);
                    }
                });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                categoryId = categoryIdList[position];
                categoryName = categoryNameList[position];
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

        formEditTexts.get(2).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                try{

                    if (formEditTexts.get(2).testValidity()){
                        if (Integer.parseInt(formEditTexts.get(2).getText().toString().trim())<0){
                            formEditTexts.get(2).setText("0");
                            formEditTexts.get(2).setSelection(formEditTexts.get(2).getText().toString().trim().length());
                        } else if (Integer.parseInt(formEditTexts.get(2).getText().toString().trim())>100){
                            formEditTexts.get(2).setText("100");
                            formEditTexts.get(2).setSelection(formEditTexts.get(2).getText().toString().trim().length());
                        }
                    } else {
                        formEditTexts.get(2).setText("0");
                        formEditTexts.get(2).setSelection(formEditTexts.get(2).getText().toString().trim().length());
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        formEditTexts.get(3).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                try{

                    if (formEditTexts.get(3).testValidity()){
                        if (Integer.parseInt(formEditTexts.get(3).getText().toString().trim())<0){
                            formEditTexts.get(3).setText("0");
                            formEditTexts.get(3).setSelection(formEditTexts.get(3).getText().toString().trim().length());
                        } else if (Integer.parseInt(formEditTexts.get(3).getText().toString().trim())>100){
                            formEditTexts.get(3).setText("100");
                            formEditTexts.get(3).setSelection(formEditTexts.get(3).getText().toString().trim().length());
                        }
                    } else {
                        formEditTexts.get(3).setText("0");
                        formEditTexts.get(3).setSelection(formEditTexts.get(3).getText().toString().trim().length());
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        formEditTexts.get(4).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                try{

                    if (formEditTexts.get(4).testValidity()){
                        if (Integer.parseInt(formEditTexts.get(4).getText().toString().trim())<0){
                            formEditTexts.get(4).setText("0");
                            formEditTexts.get(4).setSelection(formEditTexts.get(4).getText().toString().trim().length());
                        } else if (Integer.parseInt(formEditTexts.get(24).getText().toString().trim())>100){
                            formEditTexts.get(4).setText("100");
                            formEditTexts.get(4).setSelection(formEditTexts.get(4).getText().toString().trim().length());
                        }
                    } else {
                        formEditTexts.get(4).setText("0");
                        formEditTexts.get(4).setSelection(formEditTexts.get(4).getText().toString().trim().length());
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;

    }

    @OnClick({R.id.save, R.id.chooseProduct, R.id.productSize, R.id.productBagSize})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:

                if (taxType.equals("Taxable")) {
                    if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity() &&
                            formEditTexts.get(3).testValidity() && formEditTexts.get(4).testValidity()) {
                        if (imageView.getDrawable() == null) {
                            productImage = "";
                        } else {
                            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            productImage = getStringImage(bitmap);
                        }

                        addProduct(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString(), formEditTexts.get(2).getText().toString().trim(), formEditTexts.get(3).getText().toString().trim(), formEditTexts.get(4).getText().toString().trim(), prodsizeId, productbagId, productImage, categoryId);

                    }
                } else {
                    if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity()) {
                        if (imageView.getDrawable() == null) {
                            productImage = "";
                        } else {
                            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            productImage = getStringImage(bitmap);
                        }

                        addProduct(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString(), "0", "0", "0", prodsizeId, productbagId, productImage, categoryId);

                    }
                }

                break;

            case R.id.chooseProduct:
                choosePhotoHelper.showChooser();
                break;

            case R.id.productSize:

                boolean[] checkedSizeName = new boolean[productSizeNameList.length];
                boolean[] checkedSizeId = new boolean[productSizeIdList.length];
                int sizeName = productSizeNameList.length;
                int sizeId = productSizeIdList.length;

                for(int i = 0; i < sizeName; i++) {
                    checkedSizeName[i] = productSizeName.contains(productSizeNameList[i]);
                }

                for (int j = 0; j < sizeId; j++) {
                    checkedSizeId[j] = productSizeId.contains(productSizeIdList[j]);
                }

                DialogInterface.OnMultiChoiceClickListener sizeDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            productSizeName.add(productSizeNameList[which]);
                            productSizeId.add(productSizeIdList[which]);
                        }else {
                            productSizeName.remove(productSizeNameList[which]);
                            productSizeId.remove(productSizeIdList[which]);
                        }
                        onChangeSelectedProductSize();
                    }
                };

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Select Product Size");
                builder1.setMultiChoiceItems(productSizeNameList, checkedSizeName, sizeDialogListener);
                AlertDialog dialog1 = builder1.create();
                dialog1.show();

                break;

            case R.id.productBagSize:

                boolean[] checkedBagName = new boolean[productBagSizeList.length];
                boolean[] checkedBagId = new boolean[productBageIdList.length];
                int bagName = productBagSizeList.length;
                int bagId = productBageIdList.length;

                for(int i = 0; i < bagName; i++) {
                    checkedBagName[i] = productBagSizeName.contains(productBagSizeList[i]);
                }

                for (int j = 0; j < bagId; j++) {
                    checkedBagId[j] = productBagSizeId.contains(productBageIdList[j]);
                }

                DialogInterface.OnMultiChoiceClickListener bagsizeDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            productBagSizeName.add(productBagSizeList[which]);
                            productBagSizeId.add(productBageIdList[which]);
                        }else {
                            productBagSizeName.remove(productBagSizeList[which]);
                            productBagSizeId.remove(productBageIdList[which]);
                        }
                        onChangeSelectedProductBagSize();
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Product Size");
                builder.setMultiChoiceItems(productBagSizeList, checkedBagName, bagsizeDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
    }

    private void onChangeSelectedProductBagSize() {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();

        for(String sizeId : productBagSizeId) {
            stringBuilder.append(sizeId + ",");
        }
        productbagId = stringBuilder.toString();
        for (String sizeName : productBagSizeName){
            stringBuilder1.append(sizeName + ", ");
        }

        productBagSize.setText(stringBuilder1.toString());

    }

    private void onChangeSelectedProductSize() {

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();

        for(String sizeId : productSizeId) {
            stringBuilder.append(sizeId + ",");
        }
        prodsizeId = stringBuilder.toString();
        for (String sizeName : productSizeName){
            stringBuilder1.append(sizeName + ", ");
        }

        productSize.setText(stringBuilder1.toString());

    }

    private String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        choosePhotoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void addProduct(String botanicalName, String productName, String cgst, String sgst, String igst, String productSize, String productBagSize, String productImage, String categoryId) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Product is in creating");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<LoginResponse> call  = apiInterface.addProduct(MainPage.userId, botanicalName, productName, cgst, sgst, igst, productSize, productBagSize, productImage, categoryId);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                if (response.body().getSuccess().equals("true")){
                    String productId = response.body().getProductId();
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainPage) getActivity()).removeCurrentFragmentAndMoveBack();
                    AddProductAmount addProductAmount = new AddProductAmount();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productId);
                    bundle.putString("productName", productName);
                    addProductAmount.setArguments(bundle);
                    ((MainPage) getActivity()).loadFragment(addProductAmount, true);
                }else if (response.body().getSuccess().equals("false")){
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        MainPage.drawerLayout.closeDrawers();
        MainPage.importProduct.setVisibility(View.GONE);
        MainPage.saveProduct.setVisibility(View.GONE);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getCategoryList();
            getPlantSizeList();
            getBagSizeList();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBagSizeList() {

        bagSizeResponseList.clear();

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getAllBagSizeList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                bagSizeResponseList = allList.getBagSizeResponseList();
                if (bagSizeResponseList.size() == 0){
                    Toast.makeText(getActivity(), "No Bag Size Found", Toast.LENGTH_SHORT).show();
                } else {

                    productBageIdList = new String[bagSizeResponseList.size()];
                    productBagSizeList = new String[bagSizeResponseList.size()];

                    for (int i=0;i<bagSizeResponseList.size();i++){

                        productBageIdList[i] = bagSizeResponseList.get(i).getSizeId();
                        productBagSizeList[i] = bagSizeResponseList.get(i).getSizeName();

                    }

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    private void getPlantSizeList() {

        sizeResponseList.clear();

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getAllPlantSizeList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                sizeResponseList = allList.getSizeResponseList();
                if (sizeResponseList.size() == 0){
                    Toast.makeText(getActivity(), "No Plant Size Found", Toast.LENGTH_SHORT).show();
                } else {

                    productSizeIdList = new String[sizeResponseList.size()];
                    productSizeNameList = new String[sizeResponseList.size()];

                    for (int i=0;i<sizeResponseList.size();i++){

                        productSizeIdList[i] = sizeResponseList.get(i).getSizeId();
                        productSizeNameList[i] = sizeResponseList.get(i).getSizeName();

                    }

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }

    private void getCategoryList() {

        ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
        Call<AllList> call = apiInterface.getSubCategoryList(MainPage.userId);
        call.enqueue(new Callback<AllList>() {
            @Override
            public void onResponse(Call<AllList> call, Response<AllList> response) {

                AllList allList = response.body();
                categoryResponseList = allList.getSubcategoryResponseList();
                if (categoryResponseList.size() == 0){
                    Toast.makeText(getActivity(), "No category Found", Toast.LENGTH_SHORT).show();
                } else {

                    categoryIdList = new String[categoryResponseList.size()];
                    categoryNameList = new String[categoryResponseList.size()];

                    for (int i=0;i<categoryResponseList.size();i++){

                        categoryIdList[i] = categoryResponseList.get(i).getSub_type_id_pk();
                        categoryNameList[i] = categoryResponseList.get(i).getSub_type_name();

                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categoryNameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<AllList> call, Throwable t) {
                Log.e("Error", ""+t.getMessage());
            }
        });

    }
}
