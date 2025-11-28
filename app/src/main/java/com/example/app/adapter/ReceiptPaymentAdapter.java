package com.example.app.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.data.model.Receipt;
import com.example.app.data.model.Transaction;
import com.example.app.ui.receiptpayment.ReceiptPaymentCheck;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReceiptPaymentAdapter extends RecyclerView.Adapter<ReceiptPaymentAdapter.ReceiptPaymentViewHolder> {
    private List<Receipt> listReceipt;

    public ReceiptPaymentAdapter(List<Receipt> listReceipt) {
        this.listReceipt = listReceipt;
    }

    public void setData(List<Receipt> listReceipt) {
        this.listReceipt = listReceipt;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiptPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipt_payment_item, parent, false);

        return new ReceiptPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptPaymentViewHolder holder, int position) {
        Receipt currReceipt = listReceipt.get(position);

        holder.receiptID.setText("Mã HĐ: " + currReceipt.getReceiptID());
        holder.amount.setText(currReceipt.getAmount() + " VND");
        holder.type.setText(currReceipt.getType());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.time.setText(
                "Hạn: " +
                sdf.format(new Date(currReceipt.getTimestamp()))
        );

        holder.img.setImageResource(
            currReceipt.getType().equals("Tiền điện") ?
                R.mipmap.ic_electric :
                currReceipt.getType().equals("Tiền nước") ?
                    R.mipmap.ic_water :
                    R.mipmap.ic_internet
        );

        holder.payment.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReceiptPaymentCheck.class);
            intent.putExtra("type", currReceipt.getType());
            intent.putExtra("amount", currReceipt.getAmount());
            intent.putExtra("receiptID", currReceipt.getReceiptID());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listReceipt.size();
    }


    public static class ReceiptPaymentViewHolder extends RecyclerView.ViewHolder {

        TextView type, amount, time, receiptID, payment;
        ImageView img;




        public ReceiptPaymentViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type);
            amount = itemView.findViewById(R.id.amount);
            receiptID = itemView.findViewById(R.id.receiptID);
            time = itemView.findViewById(R.id.time);
            payment = itemView.findViewById(R.id.payment);
            img = itemView.findViewById(R.id.img);
        }
    }
}
