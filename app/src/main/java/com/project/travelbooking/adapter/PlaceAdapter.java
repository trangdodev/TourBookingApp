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
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.travelbooking.DetailsActivity;
import com.project.travelbooking.R;
import com.project.travelbooking.model.PlaceModel;

import java.net.URL;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.RecentsViewHolder> {

    Context context;
    List<PlaceModel> recentsDataList;

    public PlaceAdapter(Context context, List<PlaceModel> recentsDataList) {
        this.context = context;
        this.recentsDataList = recentsDataList;
    }

    @NonNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.place_row_item, parent, false);

        // here we create a recyclerview row item layout file
        return new RecentsViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecentsViewHolder holder, int position) {

        holder.rating.setRating((float)(recentsDataList.get(position).getRating()));
        holder.placeName.setText(recentsDataList.get(position).getPlaceName());
        holder.tourCount.setText(Integer.toString(recentsDataList.get(position).getTourCount()) + " hoạt động");
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL url = new URL(recentsDataList.get(position).getImageUrl());
                    Bitmap img = BitmapFactory.decodeStream(url.openStream());
                    holder.placeImage.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
//        holder.placeImage.setImageBitmap(ImageHelper.LoadImageFromUrlOnUiThread(recentsDataList.get(position).getImageUrl()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailsActivity.class);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentsDataList.size();
    }

    public static final class RecentsViewHolder extends RecyclerView.ViewHolder {

        ImageView placeImage;
        TextView placeName, tourCount;

        RatingBar rating;

        public RecentsViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.tour_image);
            placeName = itemView.findViewById(R.id.tour_name);
            tourCount = itemView.findViewById(R.id.place);
            rating = itemView.findViewById(R.id.place_rate_bar);
            rating.setIsIndicator(true);
        }
    }
}