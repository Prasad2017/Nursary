package com.sawant_nursery.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Extra.Blur;
import com.sawant_nursery.Fragment.CustomerCartList;
import com.sawant_nursery.Model.CartResponse;
import com.sawant_nursery.Model.LoginResponse;
import com.sawant_nursery.R;
import com.sawant_nursery.Retrofit.Api;
import com.sawant_nursery.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context context;
    List<CartResponse> cartResponseList;
    double totalAmount = 0f, amountPayable, taxAmount=0f;
    public static String totalAmountPayable;
    double tax=0f, delivery=0f;

    public CartAdapter(Context context, List<CartResponse> cartResponseList) {

        this.context = context;
        this.cartResponseList = cartResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CartResponse cartResponse = cartResponseList.get(position);

        totalAmount = totalAmount + (Double.parseDouble(cartResponseList.get(position).getProduct_price()) * Double.parseDouble(cartResponseList.get(position).getProduct_quantity()));

        try {
            tax =  tax+Double.parseDouble(cartResponseList.get(position).getSgst());
            delivery = delivery+Double.parseDouble(cartResponseList.get(position).getCgst());
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (cartResponseList.get(position).getTax_type().equalsIgnoreCase("Taxable")) {
                taxAmount = taxAmount + (Double.parseDouble(cartResponseList.get(position).getProduct_price()) * Double.parseDouble(cartResponseList.get(position).getProduct_quantity()));
            }  else {

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (position == cartResponseList.size() - 1) {

            holder.totalAmount.setVisibility(View.VISIBLE);
            holder.txtGurantee.setText(Html.fromHtml(context.getResources().getString(R.string.secure_payment_text)));

            holder.textViews.get(0).setText("Price (" + cartResponseList.size() + " items)");
            holder.textViews.get(1).setText(MainPage.currency + " " + totalAmount);

           // tax = Double.parseDouble(cartResponseList.get(position).getSgst());
          //  delivery = Double.parseDouble(cartResponseList.get(position).getCgst());
            Log.d("floatTax", delivery + "");
            holder.textViews.get(2).setText(" "+delivery);
            holder.textViews.get(3).setText(" " + tax);

            double taxamount = (taxAmount*(tax+delivery))/100;

            holder.textViews.get(4).setText(MainPage.currency + " " + (String.format("%.2f", (amountPayable+taxamount))));
     //       Log.d("taxAmount", String.valueOf(taxAmount));
       //     Log.d("totalAmountPayable", totalAmountPayable);
            amountPayable=totalAmount;
            holder.textViews.get(4).setText(MainPage.currency + " " + (String.format("%.2f", (amountPayable+delivery+taxamount))));

        } else
            holder.totalAmount.setVisibility(View.GONE);

        holder.productName1.setText(cartResponseList.get(position).getProduct_name());
        holder.price1.setText(MainPage.currency + " " + cartResponseList.get(position).getProduct_price());
        holder.quantity.setText("Qty: " + cartResponseList.get(position).getProduct_quantity().replace(".00", ""));
        try {

            Transformation blurTransformation = new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    Bitmap blurred = Blur.fastblur(context, source, 10);
                    source.recycle();
                    return blurred;
                }

                @Override
                public String key() {
                    return "blur()";
                }
            };

            Picasso.with(context)
                    .load("http://waghnursery.com/nursery/assets/img/"+cartResponseList.get(position).getProduct_image())
                    .placeholder(R.drawable.defaultimage)
                    .transform(blurTransformation)
                    .into(holder.image1, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            Picasso.with(context)
                                    .load("http://waghnursery.com/nursery/assets/img/"+cartResponseList.get(position).getProduct_image())
                                    .placeholder(holder.image1.getDrawable())
                                    .into(holder.image1);

                        }

                        @Override
                        public void onError() {
                        }
                    });

        }catch (Exception e) {
            e.printStackTrace();
        }

        if (!cartResponseList.get(position).getProduct_size().equalsIgnoreCase("")) {
            Log.d("size", cartResponseList.get(position).getProduct_size());
            holder.size.setText("Plant Size: " + cartResponseList.get(position).getProduct_size());
            holder.size.setVisibility(View.VISIBLE);
        } else {
            holder.size.setVisibility(View.GONE);
        }

        if (!cartResponseList.get(position).getProduct_bag_size().equalsIgnoreCase("")) {
            Log.d("bagsize", cartResponseList.get(position).getProduct_bag_size());
            holder.bagsize.setText("Bag Size: " + cartResponseList.get(position).getProduct_bag_size());
            holder.bagsize.setVisibility(View.VISIBLE);
        } else {
            holder.bagsize.setVisibility(View.GONE);
        }

        holder.actualPrice1.setText(MainPage.currency + " " + cartResponseList.get(position).getProduct_price());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading...");
                progressDialog.setTitle("Product is Deleting");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                Log.e("CartId", ""+cartResponseList.get(position).getCart_id());

                ApiInterface apiInterface = Api.getClient().create(ApiInterface.class);
                Call<LoginResponse> call = apiInterface.deleteCart(cartResponseList.get(position).getCart_id());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.body().getSuccess().equals("true")){
                            progressDialog.dismiss();
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            CustomerCartList customerCartList = new CustomerCartList();
                            Bundle bundle = new Bundle();
                            bundle.putString("customerType", cartResponseList.get(position).getType());
                            bundle.putString("customerId", cartResponseList.get(position).getC_id());
                            bundle.putString("customerName", cartResponseList.get(position).getBusiness_name());
                            customerCartList.setArguments(bundle);
                            ((MainPage) context).loadFragment(customerCartList, true);
                        } else if (response.body().getSuccess().equals("false")){
                            progressDialog.dismiss();
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Error", ""+t.getMessage());
                    }
                });

            }
        });

    }


    @Override
    public int getItemCount() {
        return cartResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image1;
        ImageView delete;
        TextView productName1, price1, actualPrice1, discountPercentage1, quantity, size, bagsize, txtGurantee;
        CardView cardView1;
        @BindView(R.id.totalAmount)
        LinearLayout totalAmount;
        @BindViews({R.id.txtPrice, R.id.price, R.id.delivery,  R.id.tax,  R.id.amountPayable,  R.id.txtTax})
        List<TextView> textViews;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            image1 = itemView.findViewById(R.id.productImage1);
            delete = itemView.findViewById(R.id.delete);
            productName1 = itemView.findViewById(R.id.productName1);
            size = itemView.findViewById(R.id.size);
            bagsize = itemView.findViewById(R.id.bagsize);
            price1 = itemView.findViewById(R.id.price1);
            quantity = itemView.findViewById(R.id.quantity);
            txtGurantee = itemView.findViewById(R.id.txtGurantee);
            actualPrice1 = itemView.findViewById(R.id.actualPrice1);
            discountPercentage1 = itemView.findViewById(R.id.discountPercentage1);
            cardView1 = itemView.findViewById(R.id.cardView1);

        }
    }
}
