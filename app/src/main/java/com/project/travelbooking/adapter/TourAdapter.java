package com.project.travelbooking.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.travelbooking.R;
import com.project.travelbooking.helper.CommonHelper;
import com.project.travelbooking.model.TourModel;

import java.net.URL;
import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.toursViewHolder> {

    Context context;
    List<TourModel> toursDataList;

    public TourAdapter(Context context, List<TourModel> toursDataList) {
        this.context = context;
        this.toursDataList = toursDataList;
    }

    @NonNull
    @Override
    public toursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.tour_row_item, parent, false);

        // here we create a recyclerview row item layout file
        return new toursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull toursViewHolder holder, int position) {

        holder.placeName.setText(toursDataList.get(position).getPlaceId());
        holder.tourName.setText(toursDataList.get(position).getTourName());
        holder.price.setText(CommonHelper.FormatCurrency(toursDataList.get(position).getPrice()));
        holder.origin_price.setText(CommonHelper.FormatCurrency(toursDataList.get(position).getOriginPrice()));
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL url = new URL(toursDataList.get(position).getImageUrl());
                    Bitmap img = BitmapFactory.decodeStream(url.openStream());
                    holder.tourImage.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public int getItemCount() {
        return toursDataList.size();
    }

    public static final class toursViewHolder extends RecyclerView.ViewHolder{

        ImageView tourImage;
        TextView tourName, placeName, price, origin_price;

        public toursViewHolder(@NonNull View itemView) {
            super(itemView);

            tourImage = itemView.findViewById(R.id.tour_image);
            tourName = itemView.findViewById(R.id.tour_name);
            placeName = itemView.findViewById(R.id.place);
            price = itemView.findViewById(R.id.price);
            origin_price = itemView.findViewById(R.id.origin_price);

        }
    }
}
