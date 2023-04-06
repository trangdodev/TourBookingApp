package com.project.travelbooking.model;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookingModel {
    private String BookingId;
    private String Username;
    private String Name;
    private String Phone;
    private int TourId;
    private String TourName;
    private String BookingDate;
    private int AdultQuantity;
    private double AdultPrice;
    private int ChildQuantity;
    private double ChildPrice;
    private int PaymentMethod;

    private String ImageUrl;

    public BookingModel() {
    }

    public BookingModel(String bookingId, String username, String name, String phone, int tourId, String tourName, String bookingDate, int adultQuantity, double adultPrice, int childQuantity, double childPrice, int paymentMethod, String imageUrl) {
        BookingId = bookingId;
        Username = username;
        Name = name;
        Phone = phone;
        TourId = tourId;
        TourName = tourName;
        BookingDate = bookingDate;
        AdultQuantity = adultQuantity;
        AdultPrice = adultPrice;
        ChildQuantity = childQuantity;
        ChildPrice = childPrice;
        PaymentMethod = paymentMethod;
        ImageUrl = imageUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("BookingId", BookingId);
        result.put("Username", Username);
        result.put("Name", Name);
        result.put("Phone", Phone);
        result.put("TourId", TourId);
        result.put("TourName", TourName);
        result.put("BookingDate", BookingDate);
        result.put("AdultQuantity", AdultQuantity);
        result.put("AdultPrice", AdultPrice);
        result.put("ChildQuantity", ChildQuantity);
        result.put("ChildPrice", ChildPrice);
        result.put("PaymentMethod", PaymentMethod);
        result.put("ImageUrl", ImageUrl);

        return result;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        BookingId = bookingId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getTourId() {
        return TourId;
    }

    public void setTourId(int tourId) {
        TourId = tourId;
    }

    public String getTourName() {
        return TourName;
    }

    public void setTourName(String tourName) {
        TourName = tourName;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public int getAdultQuantity() {
        return AdultQuantity;
    }

    public void setAdultQuantity(int adultQuantity) {
        AdultQuantity = adultQuantity;
    }

    public double getAdultPrice() {
        return AdultPrice;
    }

    public void setAdultPrice(double adultPrice) {
        AdultPrice = adultPrice;
    }

    public int getChildQuantity() {
        return ChildQuantity;
    }

    public void setChildQuantity(int childQuantity) {
        ChildQuantity = childQuantity;
    }

    public double getChildPrice() {
        return ChildPrice;
    }

    public void setChildPrice(double childPrice) {
        ChildPrice = childPrice;
    }

    public int getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
