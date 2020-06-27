package com.nursery.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nursery.Activity.MainPage;
import com.nursery.Fragment.UpdateCategory;
import com.nursery.Fragment.UpdatePlantSize;
import com.nursery.Model.CategoryResponse;
import com.nursery.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    List<CategoryResponse> categoryResponseList;

    public CategoryAdapter(Context context, List<CategoryResponse> categoryResponseList) {

        this.context = context;
        this.categoryResponseList = categoryResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CategoryResponse categoryResponse = categoryResponseList.get(position);

        holder.categoryName.setText(categoryResponseList.get(position).getCategoryType());
        holder.subCategoryName.setText(categoryResponseList.get(position).getSub_type_name());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateCategory updateCategory = new UpdateCategory();
                Bundle bundle = new Bundle();
                bundle.putString("categoryId", categoryResponseList.get(position).getCategoryId());
                bundle.putString("categoryName", categoryResponseList.get(position).getCategoryType());
                bundle.putString("subCategoryId", categoryResponseList.get(position).getSub_type_id_pk());
                bundle.putString("subCategoryName", categoryResponseList.get(position).getSub_type_name());
                updateCategory.setArguments(bundle);
                ((MainPage) context).loadFragment(updateCategory, true);

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName, subCategoryName, view;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);
            subCategoryName = itemView.findViewById(R.id.subCategoryName);
            view = itemView.findViewById(R.id.view);

        }
    }
}
