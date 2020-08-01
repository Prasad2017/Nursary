package com.sawant_nursery.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Extra.Blur;
import com.sawant_nursery.Model.ProductResponse;
import com.sawant_nursery.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class SuperAdminProductAdapter extends RecyclerView.Adapter<SuperAdminProductAdapter.MyViewHolder> {

    public static Context context;
    public static List<ProductResponse> categoryResponseList;


    public SuperAdminProductAdapter(Context context, List<ProductResponse> categoryResponseList) {

        SuperAdminProductAdapter.context = context;
        SuperAdminProductAdapter.categoryResponseList = categoryResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.super_admin_product_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProductResponse productResponse = categoryResponseList.get(position);

        holder.categoryName.setText(categoryResponseList.get(position).getProductName());

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
                    .load("http://waghnursery.com/nursery/assets/img/"+categoryResponseList.get(position).getCategoryImage())
                    .placeholder(R.drawable.defaultimage)
                    .transform(blurTransformation)
                    .into(holder.categoryIcon, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                            Picasso.with(context)
                                    .load("http://waghnursery.com/nursery/assets/img/"+categoryResponseList.get(position).getCategoryImage())
                                    .placeholder(holder.categoryIcon.getDrawable())
                                    .into(holder.categoryIcon);

                        }

                        @Override
                        public void onError() {
                        }
                    });

        }catch (Exception e) {
            e.printStackTrace();
        }

        holder.checkBox.setChecked(categoryResponseList.get(position).getSelected());
        holder.checkBox.setTag(position);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) holder.checkBox.getTag();

                if (categoryResponseList.get(pos).getSelected()) {
                    categoryResponseList.get(pos).setSelected(false);
                } else {
                    categoryResponseList.get(pos).setSelected(true);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName;
        public ImageView categoryIcon;
        public CheckBox checkBox;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}

