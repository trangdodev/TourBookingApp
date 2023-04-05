package com.project.travelbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.travelbooking.helper.CommonHelper;
import com.project.travelbooking.helper.TravelBookingDbHelper;
import com.project.travelbooking.model.TourModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    Map user = new HashMap<String, Object>();
    TourModel tour;
    ImageView detailTourImage;
    TextView detailTourName, detailAddress, detailTourRating, detailDesc, tvDuration, detailPrice, detailOriginPrice;
    ListView listSchedule;
    Button btnBookTour;
    ImageButton btnGoBackInDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        user = (Map) getIntent().getSerializableExtra("user");
        int tourId = getIntent().getIntExtra("tour", 1);

        detailTourImage = findViewById(R.id.detail_tour_img);
        detailTourName = findViewById(R.id.detail_tour_name);
        detailTourRating = findViewById(R.id.detail_tour_rating);
        detailAddress = findViewById(R.id.detail_address);
        detailDesc = findViewById(R.id.detail_desc);
        listSchedule = findViewById(R.id.listSchedule);
        tvDuration = findViewById(R.id.tvDuration);
        detailPrice = findViewById(R.id.detail_price);
        detailOriginPrice = findViewById(R.id.detail_origin_price);
        btnBookTour = findViewById(R.id.btnBookTour);
        btnGoBackInDetail = findViewById(R.id.btnGoBackInDetail);
        btnGoBackInDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBookTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, BookingActivity.class);
                i.putExtra("tour", (Serializable)tour.toMap());
                i.putExtra("user", (Serializable)user);
                DetailsActivity.this.startActivity(i);
            }
        });
        TravelBookingDbHelper.getTourById(new Callback<TourModel>() {
            @Override
            public void callback(TourModel tour) {
                DetailsActivity.this.tour = tour;
                Picasso.get().load(tour.getImageUrl()).into(detailTourImage);
                detailTourName.setText(tour.getTourName());
                detailAddress.setText(tour.getAddress());
                detailTourRating.setText(Float.toString(tour.getRating()));
                detailDesc.setText(tour.getDescription());
                tvDuration.setText(tour.getDurationTimeOfTour());
                detailPrice.setText(CommonHelper.FormatCurrency(tour.getPrice()));
                detailOriginPrice.setText(CommonHelper.FormatCurrency(tour.getOriginPrice()));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailsActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, tour.getScheduleOfTour());
                listSchedule.setAdapter(adapter);
                setListViewHeightBasedOnChildren(listSchedule);

            }
        }, tourId);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}