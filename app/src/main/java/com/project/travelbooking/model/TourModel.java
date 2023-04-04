package com.project.travelbooking.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class TourModel {
    public TourModel(int tourId, int placeId, String tourName, String address, float rating, double price, double originPrice, String promotionLabel, String imageUrl,  String durationTimeOfTour, List<List<String>> scheduleOfTour, String description) {
        TourId = tourId;
        PlaceId = placeId;
        TourName = tourName;
        Address = address;
        Rating = rating;
        Price = price;
        OriginPrice = originPrice;
        PromotionLabel = promotionLabel;
        ImageUrl = imageUrl;
        DurationTimeOfTour = durationTimeOfTour;
        ScheduleOfTour = scheduleOfTour;
        Description = description;
    }

    public int getTourId() {
        return TourId;
    }

    public void setTourId(int tourId) {
        TourId = tourId;
    }

    private int TourId;
    private int PlaceId;
    private String TourName;
    private String Address;
    private float Rating;
    private double Price;
    private double OriginPrice;
    private String PromotionLabel;
    private String ImageUrl;
    private String DurationTimeOfTour;
    private List<List<String>> ScheduleOfTour;
    private String Description;


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("TourId", TourId);
        result.put("PlaceId", PlaceId);
        result.put("Address", Address);
        result.put("TourName", TourName);
        result.put("Rating", Rating);

        result.put("Price", Price);
        result.put("OriginPrice", OriginPrice);
        result.put("PromotionLabel", PromotionLabel);

        result.put("ImageUrl", ImageUrl);
        result.put("DurationTimeOfTour", DurationTimeOfTour);
        result.put("ScheduleOfTour", ScheduleOfTour);
        result.put("Description", Description);

        return result;
    }

    public int getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(int placeId) {
        PlaceId = placeId;
    }

    public String getTourName() {
        return TourName;
    }

    public void setTourName(String tourName) {
        TourName = tourName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getOriginPrice() {
        return OriginPrice;
    }

    public void setOriginPrice(double originPrice) {
        OriginPrice = originPrice;
    }

    public String getPromotionLabel() {
        return PromotionLabel;
    }

    public void setPromotionLabel(String promotionLabel) {
        this.PromotionLabel = promotionLabel;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDurationTimeOfTour() {
        return DurationTimeOfTour;
    }

    public void setDurationTimeOfTour(String durationTimeOfTour) {
        DurationTimeOfTour = durationTimeOfTour;
    }

    public List<List<String>> getScheduleOfTour() {
        return ScheduleOfTour;
    }

    public void setScheduleOfTour(List<List<String>> scheduleOfTour) {
        ScheduleOfTour = scheduleOfTour;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
