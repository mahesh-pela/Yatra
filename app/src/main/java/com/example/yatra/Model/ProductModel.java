package com.example.yatra.Model;

import java.util.List;

public class ProductModel {
    String hotel_name;
    String location;
    String description;
//    String discount;
    String room_price;
    String img_url;
    Double rating;
    String check_out_date;
    int total_price;
    int room_count;
    String booking_id;
    String UserId;
    String check_in_date;

    String room_type;

    private double averageRating;
    private int ratingCount; // new field for the count of ratings

    // ... existing methods ...


    public ProductModel() {
        //default constructor required for Firebase while retrieving the data from the database
    }

    public ProductModel(String hotel_name, String check_out_date, int total_price, int room_count,
                        String booking_id, String UserId, String check_in_date,
                        String room_type,Double rating, String location, String description, String room_price, String img_url) {
        this.hotel_name = hotel_name;
        this.rating = rating;
        this.check_out_date = check_out_date;
        this.total_price = total_price;
        this.room_count = room_count;
        this.booking_id = booking_id;
        this.UserId = UserId;
        this.check_in_date = check_in_date;
        this.room_type = room_type;
        this.description = description;
//        this.discount = discount;
        this.room_price = room_price;
        this.img_url = img_url;
        this.location = location;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getRoom_price() {
        return room_price;
    }

    public void setRoom_price(String room_price) {
        this.room_price = room_price;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getRoom_count() {
        return room_count;
    }

    public void setRoom_count(int room_count) {
        this.room_count = room_count;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }


    public void setProductList(List<ProductModel> productList) {
        // Assuming productList is a list of ProductModel objects with ratings
        for (ProductModel product : productList) {
            if (product.getHotel_name().equals(this.hotel_name)) {
                // Set the fields of this object based on the corresponding object in the list
                this.hotel_name = product.getHotel_name();
                this.rating = product.getRating();
                this.averageRating = product.getAverageRating();
                // Set other fields as needed
                break;
            }
        }
    }


}
