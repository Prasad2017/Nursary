package com.sawant_nursery.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Fragment.LedgerResponse;

import java.util.List;

public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.MyViewHolder> {

    Context context;
    List<LedgerResponse> ledgerResponseList;

    public LedgerAdapter(Context context, List<LedgerResponse> ledgerResponseList) {

        this.context = context;
        this.ledgerResponseList = ledgerResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return ledgerResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
