package com.project.travelbooking.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PlaceModel {
    private int PlaceId;
    private String PlaceName;
    private String ImageUrl;

    private String Description;

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    private int Rating;
    @Exclude
    public int getTourCount() {
        return TourCount;
    }
    @Exclude
    public void setTourCount(int tourCount) {
        TourCount = tourCount;
    }
    @Exclude
    private int TourCount = -1;

    public PlaceModel() {
    }

    public PlaceModel(int placeId, String placeName, int rating,String imageUrl, String description) {
        PlaceId = placeId;
        PlaceName = placeName;
        Rating = rating;
        ImageUrl = imageUrl;
        Description = description;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("PlaceId", PlaceId);
        result.put("PlaceName", PlaceName);
        result.put("Rating", Rating);
        result.put("ImageUrl", ImageUrl);
        result.put("Description", Description);

        return result;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(int placeId) {
        PlaceId = placeId;
    }
}
