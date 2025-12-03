package com.example.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.data.model.FlightOfferResponse.Offer.Slice.Segment;
import com.example.app.data.model.FlightOfferResponse.Offer;
import com.example.app.ui.bookroom.BookRoomCheck;
import com.example.app.ui.bookroom.BookRoomList;
import com.example.app.ui.bookticket.BookTicketCheck;
import com.example.app.ui.bookticket.BookTicketList;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookTicketAdapter extends RecyclerView.Adapter<BookTicketAdapter.BookTicketViewHolder> {
    private List<Offer> listOffer;


    public BookTicketAdapter(List<Offer> listOffer) {
        this.listOffer = listOffer;
    }

    public void setData(List<Offer> listOffer) {
        this.listOffer = listOffer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookTicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_ticket_item, parent, false);

        return new BookTicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookTicketViewHolder holder, int position) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        Offer currOffer = listOffer.get(position);

        String nameAirlineStr = currOffer.getOwner().getName();
        holder.nameAirline.setText(nameAirlineStr);

        String durationStr = currOffer.getSlices().get(0).getDuration();
        holder.duration.setText(durationStr);


        Segment segment = currOffer.getSlices().get(0).getSegments().get(0);
        String departure = segment.getDepartingAt().substring(11, 16);
        String arrive = segment.getArrivingAt().substring(11, 16);
        holder.departureArriveTime.setText(departure + "-" + arrive);

        String priceStr = formatter.format(
                Long.parseLong(
                        currOffer.getTotalAmount()
                        .replaceAll("\\.", "")
                ) * 100
        );
        holder.price.setText(priceStr + " VND");

        holder.book.setOnClickListener(v -> {
            Context context = v.getContext();

            Intent intentBookTicketCheck = new Intent(context, BookTicketCheck.class);
            Intent intentSource = ((BookTicketList) context).getIntent();

            intentBookTicketCheck.putExtra("origin",
                    intentSource.getStringExtra("origin"));

            intentBookTicketCheck.putExtra("destination",
                    intentSource.getStringExtra("quantityPeople"));

            intentBookTicketCheck.putExtra("departureDate",
                    intentSource.getStringExtra("departureDate"));

            intentBookTicketCheck.putExtra("nameAirline",
                    nameAirlineStr
            );

            intentBookTicketCheck.putExtra("departureArriveTime",
                    departure + "-" + arrive
            );

            intentBookTicketCheck.putExtra("duration",
                    durationStr
            );

            intentBookTicketCheck.putExtra("price", priceStr);

            context.startActivity(intentBookTicketCheck);
        });
    }

    @Override
    public int getItemCount() {
        return listOffer.size();
    }


    public static class BookTicketViewHolder extends RecyclerView.ViewHolder {

        TextView nameAirline, duration, departureArriveTime, price, book;


        public BookTicketViewHolder(@NonNull View itemView) {
            super(itemView);

            nameAirline = itemView.findViewById(R.id.nameAirline);
            duration = itemView.findViewById(R.id.duration);
            departureArriveTime = itemView.findViewById(R.id.departureArriveTime);
            price = itemView.findViewById(R.id.price);
            book = itemView.findViewById(R.id.book);
        }
    }
}
