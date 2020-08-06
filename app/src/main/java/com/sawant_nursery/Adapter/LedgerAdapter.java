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

        holder.billNumber.setText(ledgerResponseList.get(position).getOrderNumber());
        holder.date.setText(ledgerResponseList.get(position).getPaymentDate());
        holder.voucherType.setText(ledgerResponseList.get(position).getVoucherType());

        if (ledgerResponseList.get(position).getVoucherType().equalsIgnoreCase("Sales")) {
            holder.debit.setText(ledgerResponseList.get(position).getOrderAmount());
            holder.credit.setText("0.00");
        } else if (ledgerResponseList.get(position).getVoucherType().equalsIgnoreCase("Purchase")) {
            holder.credit.setText(ledgerResponseList.get(position).getOrderPaidAmount());
            holder.debit.setText("0.00");
        }



    }

    @Override
    public int getItemCount() {
        return ledgerResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView billNumber, date, voucherType, debit, credit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            billNumber = (TextView) itemView.findViewById(R.id.billNumber);
            date = (TextView) itemView.findViewById(R.id.date);
            voucherType = (TextView) itemView.findViewById(R.id.voucherType);
            debit = (TextView) itemView.findViewById(R.id.debit);
            credit = (TextView) itemView.findViewById(R.id.credit);

        }
    }
}
