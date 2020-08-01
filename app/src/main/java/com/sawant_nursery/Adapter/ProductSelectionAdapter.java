package com.sawant_nursery.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Extra.Blur;
import com.sawant_nursery.Model.ProductResponse;
import com.sawant_nursery.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;


public class ProductSelectionAdapter extends RecyclerView.Adapter<ProductSelectionAdapter.MyViewHolder> {

    Context context;
    List<ProductResponse> productResponseList;


    public ProductSelectionAdapter(Context context, List<ProductResponse> productResponseList) {

        this.context = context;
        this.productResponseList = productResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProductResponse productResponse = productResponseList.get(position);

        holder.categoryName.setText(productResponseList.get(position).getProductName());

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
                    .load("http://waghnursery.com/nursery/assets/img/"+productResponseList.get(position).getCategoryImage())
                    .placeholder(R.drawable.defaultimage)
                    .transform(blurTransformation)
                    .into(holder.categoryIcon, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            Picasso.with(context)
                                    .load("http://waghnursery.com/nursery/assets/img/"+productResponseList.get(position).getCategoryImage())
                                    .placeholder(holder.categoryIcon.getDrawable())
                                    .into(holder.categoryIcon);

                        }

                        @Override
                        public void onError() {
                        }
                    });

           /* holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UpdateProductAmount updateProductAmount = new UpdateProductAmount();
                    Bundle bundle = new Bundle();
                    bundle.putString("productId", productResponseList.get(position).getProductId());

                }
            });*/

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return productResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName;
        public ImageView categoryIcon;
        public LinearLayout linearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }
}
