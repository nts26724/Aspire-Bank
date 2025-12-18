package com.example.app.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.data.model.Account;
import com.example.app.data.model.FlightOfferResponse;
import com.example.app.ui.listcustomer.ListCustomerDetail;
import com.example.app.ui.listcustomer.ListCustomerViewModel;

import java.util.List;

public class ListCustomerAdapter extends RecyclerView.Adapter<ListCustomerAdapter.ListCustomerViewHolder> {
    private List<Account> listCustomer;


    public ListCustomerAdapter(List<Account> listOffer) {
        this.listCustomer = listOffer;
    }

    public void setData(List<Account> listOffer) {
        this.listCustomer = listOffer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_customer_item, parent, false);

        return new ListCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCustomerViewHolder holder, int position) {
        Account currAccount = listCustomer.get(position);

        holder.username.setText(currAccount.getUsername());
        holder.cardNumber.setText(currAccount.getCardNumber());
        holder.balance.setText(currAccount.getBalance() + " đ");

        holder.itemLayout.setOnClickListener(v -> {
            Intent intentListCustomerDetail = new Intent(v.getContext(), ListCustomerDetail.class);
            intentListCustomerDetail.putExtra("username", currAccount.getUsername());
            v.getContext().startActivity(intentListCustomerDetail);
        });
    }

    @Override
    public int getItemCount() {
        return listCustomer.size();
    }


    public static class ListCustomerViewHolder extends RecyclerView.ViewHolder {
        private TextView username, cardNumber, balance;
        private LinearLayout itemLayout;


        public ListCustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            cardNumber = itemView.findViewById(R.id.cardNumber);
            balance = itemView.findViewById(R.id.balance);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}