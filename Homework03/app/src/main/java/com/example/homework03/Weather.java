package com.example.homework03;

import java.util.Date;

public class Weather {

    String localobservationDateTime;
    String weatherText;
    Integer weatherIcon;
    Double value;
    String unit;

    public Weather(String localobservationDateTime, String weatherText, Integer weatherIcon, Double value, String unit) {
        this.localobservationDateTime = localobservationDateTime;
        this.weatherText = weatherText;
        this.weatherIcon = weatherIcon;
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "localobservationDateTime='" + localobservationDateTime + '\'' +
                ", weatherText='" + weatherText + '\'' +
                ", weatherIcon=" + weatherIcon +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}
