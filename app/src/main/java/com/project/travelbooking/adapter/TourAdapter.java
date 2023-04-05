package com.project.travelbooking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.travelbooking.DetailsActivity;
import com.project.travelbooking.R;
import com.project.travelbooking.helper.CommonHelper;
import com.project.travelbooking.helper.RoundedCornersTransform;
import com.project.travelbooking.model.TourModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.toursViewHolder> {

    Context context;
    List<TourModel> toursDataList;
    Map user = new HashMap<String, Object>();

    public TourAdapter(Context context, List<TourModel> toursDataList, Map user) {
        this.context = context;
        this.toursDataList = toursDataList;
        this.user = user;
    }

    @NonNull
    @Override
    public toursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.tour_row_item, parent, false);

        // here we create a recyclerview row item layout file
        return new toursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull toursViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.placeName.setText(toursDataList.get(position).getPlaceName());
        holder.tourName.setText(toursDataList.get(position).getTourName());
        holder.price.setText(CommonHelper.FormatCurrency(toursDataList.get(position).getPrice()));
        holder.origin_price.setText(CommonHelper.FormatCurrency(toursDataList.get(position).getOriginPrice()));
        Picasso.get().load(toursDataList.get(position).getImageUrl()).transform(new RoundedCornersTransform()).into(holder.tourImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("tour", toursDataList.get(position).getTourId());
                i.putExtra("user", (Serializable)user);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return toursDataList.size();
    }

    public static final class toursViewHolder extends RecyclerView.ViewHolder {

        ImageView tourImage;
        TextView tourName, placeName, price, origin_price;

        public toursViewHolder(@NonNull View itemView) {
            super(itemView);

            tourImage = itemView.findViewById(R.id.tour_image);
            tourName = itemView.findViewById(R.id.tour_name);
            placeName = itemView.findViewById(R.id.place_name_of_tour);
            price = itemView.findViewById(R.id.tour_price);
            origin_price = itemView.findViewById(R.id.tour_origin_price);

        }
    }
}
