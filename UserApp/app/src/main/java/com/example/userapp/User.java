package com.example.userapp;

import java.io.Serializable;

class User {
    private int idUser;
    private String name, country;
    private int phone, age;

    public User(int idUser, String name, int Phone, String country, int age) {
        this.idUser = idUser;
        this.name = name;
        this.phone = phone;
        this.country = country;
        this.age = age;
    }

    public int getId() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public int getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }
}