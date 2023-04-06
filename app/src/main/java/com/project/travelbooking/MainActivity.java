package com.project.travelbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.travelbooking.adapter.PlaceAdapter;
import com.project.travelbooking.adapter.TourAdapter;
import com.project.travelbooking.helper.TravelBookingDbHelper;
import com.project.travelbooking.model.PlaceModel;
import com.project.travelbooking.model.TourModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {


    Map user = new HashMap<String, Object>();
    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (Map) getIntent().getSerializableExtra("loggedUserData");

        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout cartLayout = findViewById(R.id.cartLayout);
        final LinearLayout profileLayout = findViewById(R.id.profileLayout);
        final ImageView homeImage = findViewById(R.id.homeImage);
        final ImageView cartImage = findViewById(R.id.cartImage);
        final ImageView profileImage = findViewById(R.id.profileImage);
        final TextView homeTv = findViewById(R.id.homeTv);
        final TextView cartTv = findViewById(R.id.cartTv);
        final TextView profileTv = findViewById(R.id.profileTv);
        Bundle arguments = new Bundle();
        arguments.putSerializable("loggedUserData", (Serializable)user);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, HomeFragment.class, arguments)
                .commit();
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 1) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeFragment.class, arguments)
                            .commit();

                    cartTv.setVisibility(View.GONE);
                    profileTv.setVisibility(View.GONE);

                    cartImage.setImageResource(R.drawable.cart_svgrepo_com);
                    profileImage.setImageResource(R.drawable.profile_round_1346_svgrepo_com);

                    cartLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    homeTv.setVisibility(View.VISIBLE);
                    homeImage.setImageResource(R.drawable.home_selected);
                    homeLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });
        cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 2) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, CartFragment.class, arguments)
                            .commit();
                    homeTv.setVisibility(View.GONE);
                    profileTv.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home_alt_svgrepo_com);
                    profileImage.setImageResource(R.drawable.profile_round_1346_svgrepo_com);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    cartTv.setVisibility(View.VISIBLE);
                    cartImage.setImageResource(R.drawable.cart_selected);
                    cartLayout.setBackgroundResource(R.drawable.round_back_cart_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    cartLayout.startAnimation(scaleAnimation);

                    selectedTab = 2;
                }
            }
        });
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 3) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileFragment.class, arguments)
                            .commit();
                    homeTv.setVisibility(View.GONE);
                    cartTv.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.home_alt_svgrepo_com);
                    cartImage.setImageResource(R.drawable.cart_svgrepo_com);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    cartLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    profileTv.setVisibility(View.VISIBLE);
                    profileImage.setImageResource(R.drawable.profile_selected);
                    profileLayout.setBackgroundResource(R.drawable.round_back_profile_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    profileLayout.startAnimation(scaleAnimation);

                    selectedTab = 3;
                }
            }
        });
    }
}