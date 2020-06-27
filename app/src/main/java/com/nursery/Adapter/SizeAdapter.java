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
import com.nursery.Fragment.UpdatePlantSize;
import com.nursery.Model.SizeResponse;
import com.nursery.R;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {

    Context context;
    List<SizeResponse> sizeResponseList;


    public SizeAdapter(Context context, List<SizeResponse> sizeResponseList) {

        this.context = context;
        this.sizeResponseList = sizeResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SizeResponse sizeResponse = sizeResponseList.get(position);

        int pos = position+1;
        holder.srNo.setText(""+pos);
        holder.sizeName.setText(sizeResponseList.get(position).getSizeName());

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdatePlantSize updatePlantSize = new UpdatePlantSize();
                Bundle bundle = new Bundle();
                bundle.putString("plantSizeId", sizeResponseList.get(position).getSizeId());
                bundle.putString("plantSizeName", sizeResponseList.get(position).getSizeName());
                updatePlantSize.setArguments(bundle);
                ((MainPage) context).loadFragment(updatePlantSize, true);

            }
        });


    }

    @Override
    public int getItemCount() {
        return sizeResponseList.size();
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
