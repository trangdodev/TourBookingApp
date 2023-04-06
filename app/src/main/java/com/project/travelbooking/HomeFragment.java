package com.project.travelbooking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.travelbooking.adapter.PlaceAdapter;
import com.project.travelbooking.adapter.TourAdapter;
import com.project.travelbooking.helper.TravelBookingDbHelper;
import com.project.travelbooking.model.PlaceModel;
import com.project.travelbooking.model.TourModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "loggedUserData";

    // TODO: Rename and change types of parameters
    private Map user;
    RecyclerView recentRecycler, toursRecycler;
    PlaceAdapter placeAdapter;
    TourAdapter tourAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(Map param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable)param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (Map) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView tvName = getView().findViewById(R.id.tvName);
        tvName.setText(user.get("name").toString());

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
                ArrayList<Integer> placeIds = listOfTours.stream().map(tour -> tour.getPlaceId()).distinct().collect(Collectors
                        .toCollection(ArrayList::new));
                TravelBookingDbHelper.getPlaceNamesFromPlaceIds(new Callback<ArrayList<PlaceModel>>() {
                    @Override
                    public void callback(ArrayList<PlaceModel> places) {
                        listOfTours.forEach(tour -> {
                            PlaceModel place = places.stream().filter(p -> p.getPlaceId() == tour.getPlaceId()).findFirst().get();
                            tour.setPlaceName(place.getPlaceName());
                            setToursRecycler(listOfTours);
                        });
                    }
                }, placeIds);
            }
        });
    }


    private void setPlacesRecycler(List<PlaceModel> placesDataList) {

        recentRecycler = getView().findViewById(R.id.recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        placeAdapter = new PlaceAdapter(this.getContext(), placesDataList);
        recentRecycler.setAdapter(placeAdapter);

    }

    private void setToursRecycler(List<TourModel> toursDataList) {

        toursRecycler = getView().findViewById(R.id.top_places_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        toursRecycler.setLayoutManager(layoutManager);
        tourAdapter = new TourAdapter(this.getContext(), toursDataList, user);
        toursRecycler.setAdapter(tourAdapter);
    }
}