package com.sawant_nursery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Fragment.CustomerRegistration;
import com.sawant_nursery.Fragment.CustomerSelection;
import com.sawant_nursery.Model.CategoryResponse;
import com.sawant_nursery.Model.CustomerResponse;
import com.sawant_nursery.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerTxtAdapter extends RecyclerView.Adapter<CustomerTxtAdapter.MyViewHolder> {

    Context context;
    List<CustomerResponse> countryResponseList;

    public CustomerTxtAdapter(Context context, List<CustomerResponse> countryResponseList) {

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

        CustomerResponse customerResponse = countryResponseList.get(position);

        holder.countryName.setText(countryResponseList.get(position).getCustomerName());

        holder.countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);

                CustomerSelection.customerNameTxt.setText(countryResponseList.get(position).getCustomerName());
                CustomerSelection.customerId = countryResponseList.get(position).getCustomerId();
                CustomerSelection.customerName = countryResponseList.get(position).getCustomerName();
                CustomerSelection.customerState = countryResponseList.get(position).getState();
                CustomerSelection.dialog.dismiss();

            }
        });

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

