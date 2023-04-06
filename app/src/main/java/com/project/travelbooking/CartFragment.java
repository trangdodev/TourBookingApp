package com.project.travelbooking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.project.travelbooking.adapter.BookingAdapter;
import com.project.travelbooking.helper.TravelBookingDbHelper;
import com.project.travelbooking.model.BookingModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "loggedUserData";

    // TODO: Rename and change types of parameters
    private Map user;
    RecyclerView bookingRecycler;
    BookingAdapter bookingAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(Map param1) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) param1);
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TravelBookingDbHelper.getAllBookingsByUsername(new Callback<ArrayList<BookingModel>>() {
            @Override
            public void callback(ArrayList<BookingModel> data) {
                setBookingsRecycler(data);
                LinearLayout notFoundContainer = getView().findViewById(R.id.notFoundContainer);
                if (data.stream().count() > 0) {
                    notFoundContainer.setVisibility(View.GONE);
                } else {
                    notFoundContainer.setVisibility(View.VISIBLE);
                }
            }
        }, user.get("username").toString());
    }

    private void setBookingsRecycler(List<BookingModel> bookingModels) {

        bookingRecycler = getView().findViewById(R.id.bookings_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        bookingRecycler.setLayoutManager(layoutManager);
        bookingAdapter = new BookingAdapter(this.getContext(), bookingModels);
        bookingRecycler.setAdapter(bookingAdapter);

    }
}