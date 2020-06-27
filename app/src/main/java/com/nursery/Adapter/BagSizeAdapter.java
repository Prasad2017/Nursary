package com.nursery.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nursery.Activity.MainPage;
import com.nursery.Fragment.UpdateBagSize;
import com.nursery.Fragment.UpdatePlantSize;
import com.nursery.Model.BagSizeResponse;
import com.nursery.Model.SizeResponse;
import com.nursery.R;

import java.util.List;

public class BagSizeAdapter extends RecyclerView.Adapter<BagSizeAdapter.MyViewHolder> {

    Context context;
    List<BagSizeResponse> bagSizeResponseList;


    public BagSizeAdapter(Context context, List<BagSizeResponse> bagSizeResponseList) {

        this.context = context;
        this.bagSizeResponseList = bagSizeResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BagSizeResponse bagSizeResponse = bagSizeResponseList.get(position);

        int pos = position+1;
        holder.srNo.setText(""+pos);
        holder.sizeName.setText(bagSizeResponseList.get(position).getSizeName());

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateBagSize updateBagSize = new UpdateBagSize();
                Bundle bundle = new Bundle();
                bundle.putString("plantSizeId", bagSizeResponseList.get(position).getSizeId());
                bundle.putString("plantSizeName", bagSizeResponseList.get(position).getSizeName());
                updateBagSize.setArguments(bundle);
                ((MainPage) context).loadFragment(updateBagSize, true);

            }
        });


    }

    @Override
    public int getItemCount() {
        return bagSizeResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView srNo, sizeName, action;
        public LinearLayout linearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            srNo = itemView.findViewById(R.id.srNo);
            sizeName = itemView.findViewById(R.id.sizeName);
            action = itemView.findViewById(R.id.action);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }
}
