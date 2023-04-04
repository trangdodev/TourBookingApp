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
import com.project.travelbooking.model.TourModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    RecyclerView recentRecycler, topPlacesRecycler;
    PlaceAdapter placeAdapter;
    TourAdapter tourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map user = new HashMap<String, Object>();
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
                            setPlacesRecycler(listOfPlaces);
                        }
                    }, placeModel.getPlaceId());
                });
            }
        });

        TravelBookingDbHelper.getAllTours(new Callback<ArrayList<TourModel>>() {
            @Override
            public void callback(ArrayList<TourModel> listOfTours) {
                ArrayList<Integer> placeIds = listOfTours.stream().map(tour -> tour.getPlaceId()).collect(Collectors
                        .toCollection(ArrayList::new));
                TravelBookingDbHelper.getPlaceNamesFromPlaceIds(new Callback<ArrayList<PlaceModel>>() {
                    @Override
                    public void callback(ArrayList<PlaceModel> places) {
                        listOfTours.forEach(tour -> {
                            PlaceModel place = places.stream().filter(p -> p.getPlaceId() == tour.getPlaceId()).findFirst().get();
                            tour.setPlaceName(place.getPlaceName());
                        });
                        setToursRecycler(listOfTours);
                    }
                }, placeIds);
            }
        });
    }

    private void setPlacesRecycler(List<PlaceModel> placesDataList) {

        recentRecycler = findViewById(R.id.recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        placeAdapter = new PlaceAdapter(this, placesDataList);
        recentRecycler.setAdapter(placeAdapter);

    }

    private void setToursRecycler(List<TourModel> toursDataList) {

        topPlacesRecycler = findViewById(R.id.top_places_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        topPlacesRecycler.setLayoutManager(layoutManager);
        tourAdapter = new TourAdapter(this, toursDataList);
        topPlacesRecycler.setAdapter(tourAdapter);
    }
}