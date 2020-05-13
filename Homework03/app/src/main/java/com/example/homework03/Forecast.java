package com.example.homework03;

import java.io.Serializable;

public class Forecast implements Serializable {
    String updatedDate;
    Double temperature;
    String forecast;
    String forecastImage, stateName;
    String countryName;
    String cityName;
    String id;
    String code;
    boolean duplicate;
    boolean favourite;
}
