package com.sawant_nursery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Model.InvoiceResponse;
import com.sawant_nursery.R;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.MyViewHolder> {

    Context context;
    List<InvoiceResponse> invoiceResponseList;


    public InvoiceAdapter(Context context, List<InvoiceResponse> invoiceResponseList) {

        this.context = context;
        this.invoiceResponseList = invoiceResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        InvoiceResponse invoiceResponse = invoiceResponseList.get(position);

        holder.orderNumber.setText(""+invoiceResponseList.get(position).getOrder_number());
        holder.totalAmount.setText("Total Amount: "+ MainPage.currency+invoiceResponseList.get(position).getTotal_amount());
        holder.orderDate.setText("Date: "+invoiceResponseList.get(position).getOrder_date());


    }

    @Override
    public int getItemCount() {
        return invoiceResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderNumber, totalAmount, orderDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderNumber = itemView.findViewById(R.id.orderNumber);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            orderDate = itemView.findViewById(R.id.orderDate);


        }
    }
}
