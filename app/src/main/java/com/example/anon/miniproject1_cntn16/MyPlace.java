package com.example.anon.miniproject1_cntn16;

import android.graphics.Bitmap;

public class MyPlace {
    private String name;
    private String description;
    private Bitmap image;
    private String address;
    private String website;
    private String email;
    private String phone;
    private Integer fav;
    private Double lat;
    private Double lng;

    public MyPlace(String name, String description, Bitmap image, String address,
                   String website, String email, String phone, Integer fav,
                   Double lat, Double lng) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.address = address;
        this.website = website;
        this.email = email;
        this.phone = phone;
        this.fav = fav;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getFav() {
        return fav;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFav(Integer fav) {
        this.fav = fav;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
