package com.project.travelbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.project.travelbooking.adapter.PlaceAdapter;
import com.project.travelbooking.adapter.TourAdapter;
import com.project.travelbooking.helper.TravelBookingDbHelper;
import com.project.travelbooking.model.PlaceModel;
import com.project.travelbooking.model.RecentsModel;
import com.project.travelbooking.model.TopPlacesModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recentRecycler, topPlacesRecycler;
    PlaceAdapter placeAdapter;
    TourAdapter tourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map user = new HashMap<String,Object>();
        user = (Map) getIntent().getSerializableExtra("loggedUserData");

        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(user.get("name").toString());
        // Now here we will add some dummy data in our model class

        TravelBookingDbHelper.getAllPlaces(new Callback<ArrayList<PlaceModel>>() {
            @Override
            public void callback(ArrayList<PlaceModel> listOfPlaces) {
                listOfPlaces.forEach(placeModel -> {
                    TravelBookingDbHelper.countTourOfPlaces(new Callback<Integer>() {
                        @Override
                        public void callback(Integer data) {
                            placeModel.setTourCount(data);
                            setRecentRecycler(listOfPlaces);
                        }
                    }, placeModel.getPlaceId());
                });
            }
        });

        List<RecentsModel> recentsDataList = new ArrayList<>();
        recentsDataList.add(new RecentsModel("AM Lake","India","From $200",R.drawable.recentimage1));
        recentsDataList.add(new RecentsModel("Nilgiri Hills","India","From $300",R.drawable.recentimage2));
        recentsDataList.add(new RecentsModel("AM Lake","India","From $200",R.drawable.recentimage1));
        recentsDataList.add(new RecentsModel("Nilgiri Hills","India","From $300",R.drawable.recentimage2));
        recentsDataList.add(new RecentsModel("AM Lake","India","From $200",R.drawable.recentimage1));
        recentsDataList.add(new RecentsModel("Nilgiri Hills","India","From $300",R.drawable.recentimage2));


        List<TopPlacesModel> topPlacesDataList = new ArrayList<>();
        topPlacesDataList.add(new TopPlacesModel("Kasimir Hill","India","$200 - $500",R.drawable.topplaces));
        topPlacesDataList.add(new TopPlacesModel("Kasimir Hill","India","$200 - $500",R.drawable.topplaces));
        topPlacesDataList.add(new TopPlacesModel("Kasimir Hill","India","$200 - $500",R.drawable.topplaces));
        topPlacesDataList.add(new TopPlacesModel("Kasimir Hill","India","$200 - $500",R.drawable.topplaces));
        topPlacesDataList.add(new TopPlacesModel("Kasimir Hill","India","$200 - $500",R.drawable.topplaces));

        setTopPlacesRecycler(topPlacesDataList);
    }

    private  void setRecentRecycler(List<PlaceModel> recentsDataList){

        recentRecycler = findViewById(R.id.recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        placeAdapter = new PlaceAdapter(this, recentsDataList);
        recentRecycler.setAdapter(placeAdapter);

    }

    private  void setTopPlacesRecycler(List<TopPlacesModel> topPlacesDataList){

        topPlacesRecycler = findViewById(R.id.top_places_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        topPlacesRecycler.setLayoutManager(layoutManager);
        tourAdapter = new TourAdapter(this, topPlacesDataList);
        topPlacesRecycler.setAdapter(tourAdapter);

    }
}