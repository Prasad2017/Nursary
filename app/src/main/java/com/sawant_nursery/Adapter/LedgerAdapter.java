package com.sawant_nursery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Model.LedgerResponse;
import com.sawant_nursery.R;

import java.util.List;

public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.MyViewHolder> {

    Context context;
    List<LedgerResponse> ledgerResponseList;
    double paidAmount = 0f;

    public LedgerAdapter(Context context, List<LedgerResponse> ledgerResponseList) {

        this.context = context;
        this.ledgerResponseList = ledgerResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledger_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        LedgerResponse ledgerResponse = ledgerResponseList.get(position);

        holder.orderDate.setText("Date "+ledgerResponseList.get(position).getPaymentDate());
        holder.pendingAmount.setText("Pending Amount: "+ MainPage.currency+ledgerResponseList.get(position).getOrderPendingAmount());
        double paidAmount = Double.parseDouble(ledgerResponseList.get(position).getOrderAmount()) - Double.parseDouble(ledgerResponseList.get(position).getOrderPendingAmount());
        holder.paidAmount.setText("Paid Amount: "+MainPage.currency+paidAmount);
        holder.totalAmount.setText("Total Amount: "+MainPage.currency+ledgerResponseList.get(position).getOrderAmount());
        holder.orderNumber.setText(""+ledgerResponseList.get(position).getOrderNumber());

    }

    @Override
    public int getItemCount() {
        return ledgerResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderNumber, totalAmount, paidAmount, pendingAmount, orderDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderNumber = itemView.findViewById(R.id.orderNumber);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            paidAmount = itemView.findViewById(R.id.paidAmount);
            pendingAmount = itemView.findViewById(R.id.pendingAmount);
            orderDate = itemView.findViewById(R.id.orderDate);

        }
    }
}
