package com.sawant_nursery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Model.CityResponse;
import com.sawant_nursery.Model.StateResponse;
import com.sawant_nursery.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityTxtAdapter extends RecyclerView.Adapter<CityTxtAdapter.MyViewHolder> {

    Context context;
    List<CityResponse> countryResponseList;

    public CityTxtAdapter(Context context, List<CityResponse> countryResponseList) {

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

        CityResponse stateResponse = countryResponseList.get(position);

        holder.countryName.setText(countryResponseList.get(position).getCity_name());

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


