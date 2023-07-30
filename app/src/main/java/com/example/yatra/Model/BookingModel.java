package com.example.yatra.Model;


public class BookingModel {
    private String room_type;
    private int room_count;
    private String hotel_name;
    private String check_in_date;
    private String check_out_date;

    private int total_price;
    private String room_price;
    private String location;

    public BookingModel() {
        // Required empty public constructor
    }

    public BookingModel(String room_type, int total_price, String room_price, String location, int room_count, String hotel_name, String check_in_date, String check_out_date) {
        this.room_type = room_type;
        this.room_count = room_count;
        this.hotel_name = hotel_name;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.room_price = room_price;
        this.total_price = total_price;
        this.location = location;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getRoom_price() {
        return room_price;
    }

    public void setRoom_price(String room_price) {
        this.room_price = room_price;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public int getRoom_count() {
        return room_count;
    }

    public void setRoom_count(int room_count) {
        this.room_count = room_count;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }
}
