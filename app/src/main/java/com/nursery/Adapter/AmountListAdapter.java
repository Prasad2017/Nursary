package com.nursery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nursery.Model.ProductResponse;
import com.nursery.R;

import java.util.List;

public class AmountListAdapter extends RecyclerView.Adapter<AmountListAdapter.MyViewHolder> {

    Context context;
    List<ProductResponse> productResponseList;

    public AmountListAdapter(Context context, List<ProductResponse> amountProductResponseList) {

        this.context = context;
        this.productResponseList = amountProductResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.amount_item_list, null);
        MyViewHolder CartListViewHolder = new MyViewHolder(context, view);
        return CartListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProductResponse productResponse = productResponseList.get(position);

        holder.productId.setText(productResponseList.get(position).getProductName());
        holder.bagSizeId.setText(productResponseList.get(position).getBagSizeName());
        holder.plantSizeId.setText(productResponseList.get(position).getBagSizeName());
        holder.retailerAmount.setText(productResponseList.get(position).getRetailPrice());
        holder.wholesalerAmount.setText(productResponseList.get(position).getWholesalerPrice());

    }

    @Override
    public int getItemCount() {
        return productResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productId, bagSizeId, plantSizeId, retailerAmount, wholesalerAmount;

        public MyViewHolder(Context context, @NonNull View itemView) {
            super(itemView);

            productId = itemView.findViewById(R.id.productId);
            bagSizeId = itemView.findViewById(R.id.bagSizeId);
            plantSizeId = itemView.findViewById(R.id.plantSizeId);
            retailerAmount = itemView.findViewById(R.id.retailerAmount);
            wholesalerAmount = itemView.findViewById(R.id.wholesalerAmount);

        }

    }

}
