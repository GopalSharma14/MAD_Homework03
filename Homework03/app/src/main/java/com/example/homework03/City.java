package com.example.homework03;

import java.io.Serializable;
import java.util.Date;

public class City implements Serializable {

    String cityname,countryName;

    String date;
    String weatherText;
    Double valueMin,valueMax;
    String unit;
    String dayPhrase;
    Integer dayIcon;
    String nightPhrase;
    Integer nightIcon;
    String mlink;

    public City(String cityname, String countryName, String date, String weatherText, Double valueMin, Double valueMax, String unit, String dayPhrase, Integer dayIcon, String nightPhrase, Integer nightIcon, String mlink) {
        this.cityname = cityname;
        this.countryName = countryName;
        this.date = date;
        this.weatherText = weatherText;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.unit = unit;
        this.dayPhrase = dayPhrase;
        this.dayIcon = dayIcon;
        this.nightPhrase = nightPhrase;
        this.nightIcon = nightIcon;
        this.mlink = mlink;
    }

    @Override
    public String toString() {
        return "City{" +
                "cityname='" + cityname + '\'' +
                ", countryName='" + countryName + '\'' +
                ", date='" + date + '\'' +
                ", weatherText='" + weatherText + '\'' +
                ", valueMin=" + valueMin +
                ", valueMax=" + valueMax +
                ", unit='" + unit + '\'' +
                ", dayPhrase='" + dayPhrase + '\'' +
                ", dayIcon=" + dayIcon +
                ", nightPhrase='" + nightPhrase + '\'' +
                ", nightIcon=" + nightIcon +
                ", mlink='" + mlink + '\'' +
                '}';
    }
}
