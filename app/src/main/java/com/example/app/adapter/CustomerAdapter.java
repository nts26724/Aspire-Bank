package com.example.app.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    public static class CustomerModel {
        public String accountNo;
        public String name;
        public String phone;
        public String email;
        public boolean isActive;

        public CustomerModel(String accountNo, String name, String phone, String email, boolean isActive) {
            this.accountNo = accountNo;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.isActive = isActive;
        }
    }

    private List<CustomerModel> customerList;

    public CustomerAdapter(List<CustomerModel> list) {
        this.customerList = list;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        CustomerModel customer = customerList.get(position);

        holder.tvAccount.setText(customer.accountNo);
        holder.tvName.setText(customer.name);
        holder.tvPhone.setText(customer.phone);
        holder.tvEmail.setText(customer.email);

        if (customer.isActive) {
            holder.tvStatus.setText("Hoạt động");
            holder.tvStatus.setTextColor(Color.parseColor("#006400"));
            holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#90EE90")));
        } else {
            holder.tvStatus.setText("Khóa");
            holder.tvStatus.setTextColor(Color.parseColor("#A04000"));
            holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCCBC")));
        }

        holder.tvStatus.setBackgroundResource(R.drawable.bg_rounded_card);
    }

    @Override
    public int getItemCount() {
        return customerList != null ? customerList.size() : 0;
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccount, tvName, tvPhone, tvEmail, tvStatus;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAccount = itemView.findViewById(R.id.tv_account_number);
            tvName = itemView.findViewById(R.id.tv_customer_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}