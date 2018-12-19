package com.example.anon.miniproject1_cntn16;

public class ShortMyPlace {
    private String name;
    private String address;
    private Integer id;

    public ShortMyPlace(String name, String address, Integer id) {
        this.name = name;
        this.address = address;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getId() {
        return id;
    }
}
