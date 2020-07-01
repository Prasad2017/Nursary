package com.sawant_nursery.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sawant_nursery.Activity.MainPage;
import com.sawant_nursery.Fragment.InvoiceValidatePin;
import com.sawant_nursery.Fragment.UpdateCustomer;
import com.sawant_nursery.Model.CustomerResponse;
import com.sawant_nursery.R;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {

    Context context;
    List<CustomerResponse> customerResponseList;
    String customerId;

    public CustomerAdapter(Context context, List<CustomerResponse> customerResponseList) {

        this.context = context;
        this.customerResponseList = customerResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custmer_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CustomerResponse customerResponse = customerResponseList.get(position);

        int pos = position+1;

        holder.businessName.setText("Name : "+customerResponseList.get(position).getCustomerName());
        holder.mobileNumber.setText("Mb. : "+customerResponseList.get(position).getMobileNumber());
        holder.contactPerson.setText("Contact Person : "+customerResponseList.get(position).getContactPerson());
        holder.contactPersonNumber.setText("Contact Person Number : "+customerResponseList.get(position).getContact_person_number());
        holder.businessAddress.setText("Address : "+customerResponseList.get(position).getAddress());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateCustomer updateCustomer = new UpdateCustomer();
                Bundle bundle = new Bundle();
                bundle.putString("customerId", customerResponseList.get(position).getCustomerId());
                bundle.putString("type", customerResponseList.get(position).getType());
                bundle.putString("customerName", customerResponseList.get(position).getCustomerName());
                bundle.putString("customerNumber", customerResponseList.get(position).getMobileNumber());
                bundle.putString("contactPerson", customerResponseList.get(position).getContactPerson());
                bundle.putString("contactPersonNumber", customerResponseList.get(position).getContact_person_number());
                bundle.putString("email", customerResponseList.get(position).getEmailId());
                bundle.putString("address", customerResponseList.get(position).getAddress());
                bundle.putString("city", customerResponseList.get(position).getCity());
                bundle.putString("state", customerResponseList.get(position).getState());
                bundle.putString("pincode", customerResponseList.get(position).getPincode());
                updateCustomer.setArguments(bundle);
                ((MainPage)context).loadFragment(updateCustomer, true);

            }
        });

        holder.invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InvoiceValidatePin invoiceValidatePin = new InvoiceValidatePin();
                Bundle bundle= new Bundle();
                bundle.putString("customerId", customerResponseList.get(position).getCustomerId());
                invoiceValidatePin.setArguments(bundle);
                ((MainPage)context).loadFragment(invoiceValidatePin, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView businessName, mobileNumber, contactPerson, contactPersonNumber, businessAddress;
        TextView invoice, view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            businessName = itemView.findViewById(R.id.customerName);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            contactPerson = itemView.findViewById(R.id.contactPerson);
            contactPersonNumber = itemView.findViewById(R.id.contactPersonNumber);
            businessAddress = itemView.findViewById(R.id.businessAddress);
            invoice = itemView.findViewById(R.id.invoice);
            view = itemView.findViewById(R.id.view);

        }
    }
}
