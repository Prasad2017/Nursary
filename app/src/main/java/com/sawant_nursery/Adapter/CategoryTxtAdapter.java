package com.sawant_nursery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Model.CategoryResponse;
import com.sawant_nursery.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryTxtAdapter extends RecyclerView.Adapter<CategoryTxtAdapter.MyViewHolder> {

    Context context;
    List<CategoryResponse> countryResponseList;

    public CategoryTxtAdapter(Context context, List<CategoryResponse> countryResponseList) {

        this.context = context;
        this.countryResponseList = countryResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.drop_down_list_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CategoryResponse categoryResponse = countryResponseList.get(position);

        holder.countryName.setText(countryResponseList.get(position).getCategoryType());

    }

    @Override
    public int getItemCount() {
        return countryResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.countryName)
        TextView countryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

