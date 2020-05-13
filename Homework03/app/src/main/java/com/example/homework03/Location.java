package com.example.homework03;

public class Location {

    String city,state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Location(String city, String state) {
        this.city = city;
        this.state = state;
    }

    @Override
   /* public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }*/

    public String toString() {
        return city  +
                ","+ state;
    }
}
