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
import com.example.app.data.model.HotelOffer;
import com.example.app.data.model.Room;
import com.example.app.data.model.Transaction;
import com.example.app.ui.bookroom.BookRoomCheck;
import com.example.app.ui.bookroom.BookRoomList;
import com.example.app.ui.bookroom.BookRoomMap;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookRoomAdapter extends RecyclerView.Adapter<BookRoomAdapter.BookRoomViewHolder> {
    private List<HotelOffer> listRoom;


    public BookRoomAdapter(List<HotelOffer> listRoom) {
        this.listRoom = listRoom;
    }

    public void setData(List<HotelOffer> listRoom) {
        this.listRoom = listRoom;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_room_item, parent, false);

        return new BookRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRoomViewHolder holder, int position) {
        HotelOffer currHotelOffer = listRoom.get(position);

        holder.name.setText(currHotelOffer.getName());
        holder.bed.setText(currHotelOffer.getBed() + " giường");
        holder.distance.setText("Khoảng " + currHotelOffer.getDistance() + " km");

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String priceStr = formatter.format(
                Double.parseDouble(
                    currHotelOffer.getPrice().split("\\.")[0]
                )
        );
        holder.price.setText(priceStr + " VND");

        holder.booking.setOnClickListener(v -> {
            Context context = v.getContext();

            Intent intentBookRoomCheck = new Intent(context, BookRoomCheck.class);
            Intent intentSource = ((BookRoomList) context).getIntent();

            intentBookRoomCheck.putExtra("checkInDate",
                    intentSource.getStringExtra("checkInDate"));

            intentBookRoomCheck.putExtra("checkOutDate",
                    intentSource.getStringExtra("checkOutDate"));

            intentBookRoomCheck.putExtra("quantityPeople",
                    intentSource.getStringExtra("quantityPeople"));

            intentBookRoomCheck.putExtra("quantityRoom",
                    intentSource.getStringExtra("quantityRoom"));

            intentBookRoomCheck.putExtra("radius",
                    intentSource.getStringExtra("radius"));


            intentBookRoomCheck.putExtra("offerID", currHotelOffer.getId());

            intentBookRoomCheck.putExtra("name", currHotelOffer.getName());

            intentBookRoomCheck.putExtra("price", priceStr);

            context.startActivity(intentBookRoomCheck);
        });
    }

    @Override
    public int getItemCount() {
        return listRoom.size();
    }


    public static class BookRoomViewHolder extends RecyclerView.ViewHolder {

        TextView name, bed, distance, price, booking;


        public BookRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            bed = itemView.findViewById(R.id.bed);
            distance = itemView.findViewById(R.id.distance);
            price = itemView.findViewById(R.id.price);
            booking = itemView.findViewById(R.id.booking);
        }
    }
}
