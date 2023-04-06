package com.project.travelbooking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.travelbooking.R;
import com.project.travelbooking.model.BookingModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    Context context;
    List<BookingModel> bookingsDataList;

    public BookingAdapter(Context context, List<BookingModel> bookingsDataList) {
        this.context = context;
        this.bookingsDataList = bookingsDataList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false);

        // here we create a recyclerview row item layout file
        return new BookingViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.BookingViewHolder holder, int position) {
        holder.tvBookingChild.setText(Integer.toString(bookingsDataList.get(position).getChildQuantity()));
        holder.tvBookingAdult.setText(Integer.toString(bookingsDataList.get(position).getAdultQuantity()));
        holder.tvBookingId.setText(bookingsDataList.get(position).getBookingId());
        holder.tvBookingDate.setText(bookingsDataList.get(position).getBookingDate());
        holder.tvBookingTourName.setText(bookingsDataList.get(position).getTourName());

        Picasso.get().load(bookingsDataList.get(position).getImageUrl()).into(holder.imgBookingTour);
    }

    @Override
    public int getItemCount() {
        return bookingsDataList.size();
    }

    public static final class BookingViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBookingTour;
        TextView tvBookingChild, tvBookingAdult, tvBookingId, tvBookingDate, tvBookingTourName;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBookingTour = itemView.findViewById(R.id.imgBookingTour);
            tvBookingChild = itemView.findViewById(R.id.tvBookingChild);
            tvBookingAdult = itemView.findViewById(R.id.tvBookingAdult);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvBookingTourName = itemView.findViewById(R.id.tvBookingTourName);
        }
    }
}
