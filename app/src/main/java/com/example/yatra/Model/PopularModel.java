package com.example.yatra.Model;

public class PopularModel {

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    String hotel_id;
    String name;
    String location;
    String description;
//    String discount;
    String price;
    String img_url;

    public PopularModel() {
    }

    public PopularModel(String name, String location, String description,String price, String img_url, String hotel_id) {
        this.name = name;
        this.description = description;
//        this.discount = discount;
        this.price = price;
        this.img_url = img_url;
        this.location = location;
        this.hotel_id = hotel_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }



}
