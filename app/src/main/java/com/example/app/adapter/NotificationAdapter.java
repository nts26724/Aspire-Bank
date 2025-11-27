package com.example.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.data.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Transaction> listNotification;

    public NotificationAdapter(List<Transaction> listNotification) {
        this.listNotification = listNotification;
    }

    public void setData(List<Transaction> listNotification) {
        this.listNotification = listNotification;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);

        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Transaction currTransaction = listNotification.get(position);

        holder.transactionID.setText(currTransaction.getTransactionID());
        holder.amount.setText(currTransaction.getAmount() + " VND");
        holder.content.setText(currTransaction.getContent());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.time.setText(
                sdf.format(new Date(currTransaction.getTimestamp()))
        );

        holder.amount.setTextColor(
                currTransaction.isTransfer() ?
                        holder.itemView.getResources().getColor(R.color.aspire_red_transfer) :
                        holder.itemView.getResources().getColor(R.color.aspire_green_receive)
        );
    }

    @Override
    public int getItemCount() {
        return listNotification.size();
    }


    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView transactionID, amount, content, time;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            transactionID = itemView.findViewById(R.id.transactionID);
            amount = itemView.findViewById(R.id.amount);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
        }
    }
}
